package SublindWay_server.controller;

import SublindWay_server.entity.ImageEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.service.NaverOCRService;
import SublindWay_server.service.OcrAnalyzer;
import SublindWay_server.service.S3Uploader;
import com.amazonaws.services.s3.AmazonS3;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController(value = "/images")
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
    private final AmazonS3 amazonS3;

    public ImageController(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @PostMapping(value = "/send-subways-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 넣기", description = "이미지 분석 By Naver Ocr")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<String> imageUploadAndCheckSubwayNum(@Parameter(description = "이미지 파일", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, ""); // 이미지를 업로드하고 반환된 S3 키(경로)를 얻음
        List<String> answer = ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
        return answer;
    }

    @GetMapping(value = "/find-image-uuid")
    @Operation(summary = "이미지 uuid찾기", description = "uuid찾기")
    @ApiResponse(responseCode = "200", description = "UUID Found", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Image not found")
    public String getImageUUID(@Parameter(description = "카카오아이디로 찾기", required = true) @RequestParam String kakaoId) throws IOException {
        List<ImageEntity> imageEntities = imageRepository.findByKakaoId(kakaoId);
        return imageEntities.get(0).getImageUUID();
    }
}
