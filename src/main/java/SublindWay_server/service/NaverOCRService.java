package SublindWay_server.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class NaverOCRService {
    @Value("${OCR_SECERET_KEY}")
    private String SECRET_KEY;
    @Value("${OCR_API}")
    private String ocr_api;
    @Autowired
    private S3Uploader s3Uploader;

    public String processOCR(String imageFilePath) {
        try {
            // 현재 작업 디렉토리에 기반한 'uploads' 폴더 경로 사용
            String baseDir = System.getProperty("user.dir");
            String fullPath = baseDir + File.separator + "uploads" + File.separator + imageFilePath + ".jpg";

            // OCR 서비스 URL 설정
            URL url = new URL(ocr_api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", SECRET_KEY);

            // JSON 객체 생성
            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");

            // 이미지 파일 읽기
            FileInputStream inputStream = new FileInputStream(fullPath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // 이미지 데이터를 Base64로 인코딩하여 JSON에 추가
            String imageDataString = Base64.encodeBase64String(buffer);
            image.put("data", imageDataString);
            image.put("name", "demo");

            // JSON 배열에 이미지 정보 추가
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            // 서버에 요청 전송
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            // 서버 응답 처리
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            // 응답 내용 읽기
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // 응답 내용 반환
            return response.toString();
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}
