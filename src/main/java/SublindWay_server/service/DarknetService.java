package SublindWay_server.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(":")) {
                        detectedObjects.add(line.substring(0, line.indexOf(":")).trim());
                    }
                }
            }

            // 에러 출력 읽기
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("ERROR: " + line);
                }
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            System.out.println("Exited with error code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return detectedObjects;
    }
}
