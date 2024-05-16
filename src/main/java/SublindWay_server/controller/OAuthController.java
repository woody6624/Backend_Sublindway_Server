package SublindWay_server.controller;

import SublindWay_server.dto.UserDTO;
import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.UserRepository;
import SublindWay_server.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {
    @Autowired
    OAuthService oAuthService;

    @Autowired
    UserRepository userRepository;
    @ResponseBody
    @GetMapping("/kakao")
    public RedirectView kakaoLoginWeb(@RequestParam String code, HttpServletRequest request) {
        String accessToken = oAuthService.getKakaoAccessToken(code);
        UserDTO user = oAuthService.createKakaoUser(accessToken);

        // 세션에 사용자 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        String userNameEncoded = "";
        try {
            userNameEncoded = URLEncoder.encode(user.getUserName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String redirectUrl = "http://localhost:3000/locationMap?kakaoId=" + user.getKakaoId() + "&userName=" + userNameEncoded;
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        oAuthService.kakaoLogout();

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "로그아웃 성공";
    }

    @GetMapping("/check-login")
    public Map<String, Boolean> checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute("user") != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLoggedIn", isLoggedIn);
        return response;
    }

    @GetMapping(value="/get-access-token")
    public String getAccessToken(String kakaoId) {
        Optional<UserEntity> userEntity = userRepository.findById(kakaoId);
        return userEntity.get().getAccessToken();
    }

}
