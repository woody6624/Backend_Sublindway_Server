package SublindWay_server.service;

import SublindWay_server.dto.UserDTO;
import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class OAuthService {
    @Autowired
    UserRepository userRepository;
    @Value("${KAKAO_CLIENT_ID}")
    String clientId;
    private String logoutRedirectUri="http://13.209.19.20:8079/oauth/logout";
    public UserDTO createKakaoUser(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        // access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); // 전송할 header 작성, access_token 전송

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    StringBuilder result = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("response body : " + result.toString());

                    // Gson 라이브러리로 JSON 파싱
                    JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();
                    long id = jsonObject.get("id").getAsLong();
                    String kakaoId = String.valueOf(id);
                    System.out.println("id : " + id);

                    // 닉네임 추출
                    String nickname = jsonObject.getAsJsonObject("properties").get("nickname").getAsString();
                    System.out.println("nickname : " + nickname);

                    Optional<UserEntity> userEntity = userRepository.findById(kakaoId);
                    if (userEntity.isPresent()) {
                        UserEntity saveEntity = userEntity.get();
                        saveEntity.setAccessToken(token);
                        userRepository.save(saveEntity);
                        UserDTO sendDto = new UserDTO();
                        sendDto.setKakaoId(saveEntity.getMuckatUserId());
                        sendDto.setUserName(saveEntity.getUserName());
                        return sendDto;
                    } else { // 해당 유저가 처음일 경우 -> 회원가입
                        UserEntity saveEntity = new UserEntity();
                        saveEntity.setMuckatUserId(kakaoId);
                        saveEntity.setUserName(nickname);
                        saveEntity.setAccessToken(token);
                        userRepository.save(saveEntity);
                        UserDTO sendDto = new UserDTO();
                        sendDto.setKakaoId(saveEntity.getMuckatUserId());
                        sendDto.setUserName(saveEntity.getUserName());
                        return sendDto; // 새로 저장된 유저의 닉네임 반환
                    }
                }
            } else {
                // 오류 응답 처리 로직
                System.out.println("서버로부터 오류 응답: " + responseCode);
                return null; // 오류가 발생하면 null 반환
            }
        } catch (IOException e) {
            System.err.println("서버 통신 중 오류 발생: ");
            e.printStackTrace();
            return null; // 예외가 발생하면 null 반환
        }
    }

    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(clientId);
            sb.append("&redirect_uri=http://13.209.19.20:8079/oauth/kakao");
            sb.append("&code=").append(code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                access_Token = element.getAsJsonObject().get("access_token").getAsString();
                refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

                System.out.println("access_token : " + access_Token);
                System.out.println("refresh_token : " + refresh_Token);

                br.close();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("Error response: " + result);
                br.close();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }


    public void kakaoLogout() {
        String reqURL = "https://kauth.kakao.com/oauth/logout";

        try {
            // URL에 쿼리 파라미터 추가
            String urlWithParams = reqURL + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&logout_redirect_uri=" + URLEncoder.encode(logoutRedirectUri, StandardCharsets.UTF_8);

            URL url = new URL(urlWithParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) { // 302 리다이렉트 응답 코드
                String location = conn.getHeaderField("Location");
                System.out.println("Redirect to: " + location);
                // 리다이렉트 URL로 이동 처리 필요
            } else {
                System.out.println("Error: Failed to logout, response code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
