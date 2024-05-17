package SublindWay_server.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;

@Service
public class YoloImageDetectionService {

    private final WebClient webClient;

    public YoloImageDetectionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://34.47.98.144:5000").build();
    }

    public String detectObjects(String imagePath) {
        String baseDir = System.getProperty("user.dir");
        String fullPath = baseDir + File.separator + "uploads" + File.separator + imagePath + ".jpg";
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image", new FileSystemResource(fullPath));

        return this.webClient.post()
                .uri("/detect")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()  // 통신 시작
                .bodyToMono(String.class)  // 반환된 데이터를 String 형태로 변환
                .block();  // 비동기 작업을 동기 작업으로 변환, 실제 요청이 이루어지고 결과를 기다림
    }
}
