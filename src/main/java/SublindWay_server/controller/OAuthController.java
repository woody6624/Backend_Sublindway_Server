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
    @Operation(summary = "웹에서 인가 코드 전달", description = "해당 인가 코드로 로그인 구현,유저의 id값 리턴")
    public RedirectView kakaoLoginWeb(@RequestParam String code) {
        String gugucaca = oAuthService.getKakaoAccessToken(code);
        System.out.println(gugucaca);
        UserDTO user = oAuthService.createKakaoUser(gugucaca);

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
    @GetMapping(value="/get-access-token")
    @Operation(summary = "유저 id로 엑세스토큰 get", description = "로그아웃 시 필요")
    public String getAccessToken(String kakaoId){
        Optional<UserEntity> userEntity =userRepository.findById(kakaoId);
        return userEntity.get().getAccessToken();
    }

}
