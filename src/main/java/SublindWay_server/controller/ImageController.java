package SublindWay_server.controller;

import SublindWay_server.dto.SendWebData;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.ImageEntity;
import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.repository.ImageRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private SseService sseService; // SSE 서비스 주입
    @PostMapping(value = "/send-subways-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 넣기", description = "이미지 분석 By Naver Ocr")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public SendWebData imageUploadAndCheckSubwayNum(@RequestParam("file") MultipartFile file, @RequestParam("kakaoId") String kakaoId,
                                                    @RequestParam("locationX") double locationX, @RequestParam("locationY") double locationY) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, kakaoId, "");
        List<String> answer = ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));

        SubwayDetailDTO subwayDetailDTO = subwaySearchServices.getSubwayDetailsByLocation(locationX, locationY);
        String direction = "";

        if (answer.contains("상행")) {
            direction = "상행";
        } else if (answer.contains("하행")) {
            direction = "하행";
        } else {
            // Pattern to match numbers in the format "2-3"
            Pattern pattern = Pattern.compile("\\d+-\\d+");
            boolean foundMatch = false;
            for (String ans : answer) {
                Matcher matcher = pattern.matcher(ans);
                if (matcher.find()) {
                    direction = ans;
                    foundMatch = true;
                    break;
                }
            }

            // If no "2-3" pattern found, parse YOLO result for direction
            if (!foundMatch) {
                String yoloResult = yoloImageDetectionService.detectObjects(s3Key);
                yoloResult = yoloResult.substring(1, yoloResult.length() - 1).replace("\\\"", "\"");

                // Create ObjectMapper instance
                ObjectMapper mapper = new ObjectMapper();

                // Parse the JSON array
                JsonNode arrayNode = mapper.readTree(yoloResult);

                // List to store the names
                List<String> names = new ArrayList<>();

                // Iterate over the array elements
                for (JsonNode node : arrayNode) {
                    String name = node.get("name").asText();
                    names.add(name);
                }

                // Join the names into a single string (or any other format you need)
                direction = String.join(", ", names);
            }
        }

        String trainNum = connectionWithRealTimeServerService.connectionWithRealSubway(subwayDetailDTO.getSubwayName(), direction);

        SendWebData sendWebData = new SendWebData(trainNum, direction, locationX, locationY);
        sseService.updateLastKnownLocation(kakaoId, sendWebData); // 도착지 데이터 업데이트
        sseService.sendEventToUser(kakaoId, sendWebData); // 클라이언트에게 SSE 이벤트 전송
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
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
        return sseService.subscribe(userId); // SSE 구독
    }

    @GetMapping("/find-image/by-kakaoId")
    @Operation(summary = "카카오id로 이미지 찾기", description = "카카오id로 이미지 찾기")
    public List<ImageEntity> imageList(@RequestParam("kakaoId") String kakaoId){
        List<ImageEntity> imageEntities=imageRepository.findByKakaoId(kakaoId);
        return imageEntities;
    }
}
