package SublindWay_server.controller;

import SublindWay_server.dto.SendWebData;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.ImageEntity;
import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.service.*;
import com.amazonaws.services.s3.AmazonS3;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @PostMapping(value = "/send-subways-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 넣기", description = "이미지 분석 By Naver Ocr")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public SendWebData imageUploadAndCheckSubwayNum(@RequestParam("file") MultipartFile file, @RequestParam("kakaoId") String kakaoId,
                                                    @RequestParam("locationX") double locationX, @RequestParam("locationY") double locationY) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, kakaoId, "");
        List<String> answer = ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));

        SubwayDetailDTO subwayDetailDTO = subwaySearchServices.getSubwayDetailsByLocation(locationX, locationY);
        String direction="";
        if(answer.contains("상행")){
            direction="상행";
        }
        else if(answer.contains("하행")){
            direction="하행";
        }
        else{
            direction="상 하행을 구분 불가능한 사진입니다";
        }
        String trainNum = connectionWithRealTimeServerService.connectionWithRealSubway(subwayDetailDTO.getSubwayName(), direction);

        SendWebData sendWebData = new SendWebData(trainNum,direction,locationX, locationY);
        lastKnownLocations.put(kakaoId, sendWebData);
        return sendWebData;
    }

    @GetMapping(value = "/find-image-uuid")
    @Operation(summary = "이미지 uuid찾기", description = "uuid찾기")
    @ApiResponse(responseCode = "200", description = "UUID Found", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Image not found")
    public String getImageUUID(@RequestParam("kakaoId") String kakaoId) throws IOException {
        List<ImageEntity> imageEntities = imageRepository.findByKakaoId(kakaoId);
        return imageEntities.get(0).getImageUUID();
    }


    @GetMapping("/stream/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        SendWebData initialLocation = lastKnownLocations.get(userId);
        if (initialLocation != null) {
            sendEventToUser(userId, initialLocation);
        }

        return emitter;
    }

    private void sendEventToUser(String userId, SendWebData sendWebData) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(sendWebData, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
