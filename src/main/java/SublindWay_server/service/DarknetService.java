package SublindWay_server.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class DarknetService {

    public List<String> runDarknet(String imagePath) {
        List<String> detectedObjects = new ArrayList<>();
        ProcessBuilder processBuilder = new ProcessBuilder();

        try {
            // darknet 디렉토리 설정
            processBuilder.directory(new File(System.getProperty("user.home"), "darknet"));
            // 명령어 실행
            processBuilder.command("bash", "-c", "./darknet detect cfg/yolov3.cfg yolov3.weights " + imagePath);

            // 프로세스 실행
            Process process = processBuilder.start();

            // 결과 출력 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("STDOUT: " + line); // 로그 추가
                    if (line.contains(":")) {
                        detectedObjects.add(line.substring(0, line.indexOf(":")).trim());
                    }
                }
            }

            // 에러 출력 읽기
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("STDERR: " + line); // 로그 추가
                }
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            System.out.println("Process exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to run darknet: " + e.getMessage());
        }

        return detectedObjects;
    }
}
