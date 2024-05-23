package SublindWay_server.service;

import SublindWay_server.entity.ImageEntity;
import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageAnalysisService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NaverOCRService naverOCRService;
    @Autowired
    private OcrAnalyzer ocrAnalyzer;
    @Autowired
    private YoloImageDetectionService yoloImageDetectionService;
    @Autowired
    private ConnectionWithRealTimeServerService connectionWithRealTimeServerService;
    @Autowired
    private S3Uploader s3Uploader;

    private final ReentrantLock lock = new ReentrantLock();

    @Transactional
    public String analyzeImageAndDetermineDirection(String s3Key, String kakaoId, double locationX, double locationY) throws IOException {
        lock.lock();
        try {
            List<String> answer = ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));
            String direction = "";

            if (answer.contains("상행")) {
                direction = "상행";
                saveImageEntity(s3Key, kakaoId, "탑승");
            } else if (answer.contains("하행")) {
                direction = "하행";
                saveImageEntity(s3Key, kakaoId, "탑승");
            } else {
                //패턴 정규식 지정
                Pattern pattern = Pattern.compile("\\d+-\\d+");
                boolean foundMatch = false;
                for (String ans : answer) {
                    Matcher matcher = pattern.matcher(ans);
                    if (matcher.find()) {
                        direction = ans;
                        foundMatch = true;
                        saveImageEntity(s3Key, kakaoId, "탑승칸");
                        break;
                    }
                }

                if (!foundMatch) {
                    String yoloResult = yoloImageDetectionService.detectObjects(s3Key);
                    yoloResult = yoloResult.substring(1, yoloResult.length() - 1).replace("\\\"", "\"");
                    saveImageEntity(s3Key, kakaoId, "욜로");

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode arrayNode = mapper.readTree(yoloResult);

                    List<String> names = new ArrayList<>();
                    for (JsonNode node : arrayNode) {
                        String name = node.get("name").asText();
                        names.add(name);
                    }
                    direction = String.join(", ", names);
                }
            }
            return direction;
        } finally {
            lock.unlock();
        }
    }

    private void saveImageEntity(String s3Key, String kakaoId, String status) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageUUID(s3Key);
        imageEntity.setLocalDateTime(LocalDate.now().atStartOfDay());
        Optional<UserEntity> userEntity = userRepository.findById(kakaoId);
        imageEntity.setUserEntity(userEntity.get());
        imageEntity.setYoloOrRideOrBoard(status);
        imageRepository.save(imageEntity);
    }
}
