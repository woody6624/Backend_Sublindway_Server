package SublindWay_server.controller;

import SublindWay_server.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/web")
public class OAuthController {
    @Autowired
    OAuthService oAuthService;
    @ResponseBody
    @GetMapping("/login")
    @Operation(summary = "웹에서 인가 코드 전달", description = "해당 인가 코드로 로그인 구현")

    public String kakaoLoginWeb(@RequestParam String code){
        String gugucaca=oAuthService.getKakaoAccessToken(code);
        System.out.println(gugucaca);
        return oAuthService.createKakaoUser(gugucaca);
    }


}
