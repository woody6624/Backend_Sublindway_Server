package SublindWay_server.controller;

import SublindWay_server.service.NaverOCRService;
import SublindWay_server.service.OcrAnalyzer;
import SublindWay_server.service.S3Uploader;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@RestController(value="/image")
public class ImageController {

    @Autowired
    S3Uploader s3Uploader;

    @Autowired
    NaverOCRService naverOCRService;
    @Autowired
    OcrAnalyzer ocrAnalyzer;
    @Autowired
    private final AmazonS3 amazonS3;

    public ImageController(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    @PostMapping(value = "/send-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "이미지 넣기", notes = "지하철 이미지 넣기")
    public void testImageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String s3Key = s3Uploader.uploadImageFile(file, ""); // 이미지를 업로드하고 반환된 S3 키(경로)를 얻음
        // OCR 서비스에 S3 키(경로)와 파일 스트림을 전달하여 OCR 수행
        ocrAnalyzer.getOcrSubwayNumList(naverOCRService.processOCR(s3Key));
        s3Uploader.removeNewFile(new File(s3Key + ".jpg"));
    }

}
