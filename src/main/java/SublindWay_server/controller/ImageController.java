package SublindWay_server.controller;

import SublindWay_server.dto.SendImagesUuidDTO;
import SublindWay_server.dto.SendWebData;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.ImageEntity;
import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.repository.UserRepository;
import SublindWay_server.service.*;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class ImageController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, SendWebData> lastKnownLocations = new ConcurrentHashMap<>();
    private final Path rootLocation = Paths.get("images");

    @Autowired
    SubwaySearchService subwaySearchServices;
    @Autowired
    S3Uploader s3Uploader;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    ConnectionWithRealTimeServerService connectionWithRealTimeServerService;
    @Autowired
    YoloImageDetectionService yoloImageDetectionService;
    @Autowired
    NaverOCRService naverOCRService;
    @Autowired
    OcrAnalyzer ocrAnalyzer;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private ImageAnalysisService imageAnalysisService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private SseService sseService; // SSE 서비스 주입
    @PostMapping(value = "/send-subways-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 넣기-승차시 처리", description = "승차 시 이미지와 본인의 kakaoId,x,y좌표를 보냅니다")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public SendWebData imageUploadAndCheckSubwayNum(@RequestParam("file") MultipartFile file, @RequestParam("kakaoId") String kakaoId,
                                                    @RequestParam("locationX") double locationX, @RequestParam("locationY") double locationY) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, kakaoId);
        String direction = imageAnalysisService.analyzeImageAndDetermineDirection(s3Key, kakaoId, locationX, locationY);

        SubwayDetailDTO subwayDetailDTO = subwaySearchServices.getSubwayDetailsByLocation(locationX, locationY);
        String trainNum = connectionWithRealTimeServerService.connectionWithRealSubway(subwayDetailDTO.getSubwayName(), direction);

        SendWebData sendWebData = new SendWebData(trainNum, direction, locationX, locationY);
        sseService.updateLastKnownLocation(kakaoId, sendWebData);
        sseService.sendEventToUser(kakaoId, sendWebData);
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
        return sendWebData;
    }
    @GetMapping(value = "/find-image-uuid")
    @Operation(summary = "이미지 uuid찾기", description = "uuid찾기")
    @ApiResponse(responseCode = "200", description = "UUID Found", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Image not found")
    public String getImageUUID(@RequestParam("kakaoId") String kakaoId) throws IOException {
        List<ImageEntity> imageEntities = imageRepository.findByKakaoIdGetRideImage(kakaoId);

        return imageEntities.get(0).getImageUUID();
    }
    @GetMapping("/stream/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        return sseService.subscribe(userId); // SSE 구독
    }

    @GetMapping("/find-image/by-kakaoId")
    @Operation(summary = "카카오id로 이미지 찾기", description = "카카오id로 이미지 찾기")
    public List<SendImagesUuidDTO> imageList(@RequestParam("kakaoId") String kakaoId){
        List<ImageEntity> imageEntities = imageRepository.findByKakaoId(kakaoId);
        return imageEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private SendImagesUuidDTO convertToDto(ImageEntity imageEntity) {
        SendImagesUuidDTO dto = new SendImagesUuidDTO();
        dto.setImageUUID(imageEntity.getImageUUID());
        dto.setMuckatUserId(imageEntity.getUserEntity().getMuckatUserId());
        dto.setUserName(imageEntity.getUserEntity().getUserName());
        dto.setLocalDateTime(imageEntity.getLocalDateTime());
        dto.setYoloOrRide(imageEntity.getYoloOrRideOrBoard());
        return dto;
    }

}
