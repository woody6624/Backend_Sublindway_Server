package SublindWay_server.controller;

import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.UserRepository;
import SublindWay_server.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
//로그인 부분
    public String kakaoLoginWeb(@RequestParam String code){
        String gugucaca=oAuthService.getKakaoAccessToken(code);
        System.out.println(gugucaca);
        return oAuthService.createKakaoUser(gugucaca);
    }

    @PostMapping(value="/logout")
    @Operation(summary = "엑세스 토큰으로 로그아웃", description = "로그아웃 구현")
    public String logout(String accessToken) {
        oAuthService.kakaoLogout(accessToken);
        return "redirect:/";
    }
    @GetMapping(value="/get-access-token")
    @Operation(summary = "유저 id로 엑세스토큰 get", description = "로그아웃 시 필요")
    public String getAccessToken(String kakaoId){
        Optional<UserEntity> userEntity =userRepository.findById(kakaoId);
        return userEntity.get().getAccessToken();
    }

}
