package SublindWay_server.service;

import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class OAuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${KAKAO_REDIRECT_URI}")
    private String redirectUri;

    public String createKakaoUser(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();
                    long id = jsonObject.get("id").getAsLong();
                    String kakaoId = String.valueOf(id);
                    String nickname = jsonObject.getAsJsonObject("properties").get("nickname").getAsString();

                    Optional<UserEntity> userEntity = userRepository.findById(kakaoId);
                    if (userEntity.isPresent()) {
                        UserEntity existingUser = userEntity.get();
                        existingUser.setAccessToken(token);
                        userRepository.save(existingUser);
                        return existingUser.getUserName();
                    } else {
                        UserEntity newUser = new UserEntity();
                        newUser.setMuckatUserId(kakaoId);
                        newUser.setUserName(nickname);
                        newUser.setAccessToken(token);
                        userRepository.save(newUser);
                        return nickname;
                    }
                }
            } else {
                System.out.println("서버로부터 오류 응답: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getKakaoAccessToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                StringBuilder sb = new StringBuilder();
                sb.append("grant_type=authorization_code");
                sb.append("&client_id=").append(clientId);
                sb.append("&redirect_uri=").append(redirectUri);
                sb.append("&code=").append(code);
                bw.write(sb.toString());
                bw.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    JsonElement element = JsonParser.parseString(result.toString());
                    return element.getAsJsonObject().get("access_token").getAsString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    StringBuilder errorResult = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResult.append(line);
                    }
                    System.out.println("Error response: " + errorResult.toString());
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void kakaoLogout(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(result);

                    // Access Token 무효화
                    Optional<UserEntity> userEntity = userRepository.findByAccessToken(accessToken);
                    userEntity.ifPresent(user -> {
                        user.setAccessToken(null);
                        userRepository.save(user);
                    });
                }
            } else {
                System.out.println("Logout failed: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
