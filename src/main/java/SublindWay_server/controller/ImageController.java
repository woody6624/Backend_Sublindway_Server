package SublindWay_server.controller;

import SublindWay_server.entity.ImageEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.service.NaverOCRService;
import SublindWay_server.service.OcrAnalyzer;
import SublindWay_server.service.S3Uploader;
import com.amazonaws.services.s3.AmazonS3;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@RestController(value= "/images")
public class ImageController {
    private final Path rootLocation = Paths.get("images");

    @Autowired
    S3Uploader s3Uploader;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    NaverOCRService naverOCRService;
    @Autowired
    OcrAnalyzer ocrAnalyzer;
    @Autowired
    private final AmazonS3 amazonS3;

    public ImageController(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @PostMapping(value = "/send-subways-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "이미지 넣기", notes = "지하철-지하철-지하철 이미지 넣기")
    public List<String> imageUploadAndCheckSubwayNum(@RequestParam("file") MultipartFile file) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, ""); // 이미지를 업로드하고 반환된 S3 키(경로)를 얻음
        // OCR 서비스에 S3 키(경로)와 파일 스트림을 전달하여 OCR 수행
        List<String> answer=ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
        return answer;
    }

    @PostMapping(value = "/send-board-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "이미지 넣기", notes = "2-3같은 탑승구 이미지 넣기")
    public List<String> imageUploadAndCheckHyphen(@RequestParam("file") MultipartFile file) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, ""); // 이미지를 업로드하고 반환된 S3 키(경로)를 얻음
        List<String> answer=ocrAnalyzer.getOcrSubwayRangeList(naverOCRService.processOCR(s3Key));
        // OCR 서비스에 S3 키(경로)와 파일 스트림을 전달하여 OCR 수행
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
        return answer;
    }

    @PostMapping(value = "/find-image-uuid")
    @ApiOperation(value = "이미지 uuid찾기", notes = "uuid찾기")
    public String getImageUUID(@RequestParam String kakaoId) throws IOException {
        List<ImageEntity> imageEntities=imageRepository.findByKakaoId(kakaoId);
        return  imageEntities.get(0).getImageUUID();

    }
}
