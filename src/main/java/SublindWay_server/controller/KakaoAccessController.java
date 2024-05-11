package SublindWay_server.controller;

import SublindWay_server.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value="/kakao-access")
public class KakaoAccessController {
    @Autowired
    OAuthService oAuthService;
    @PostMapping("/get-token")
    public void test(@RequestParam String code){
        System.out.println(oAuthService.getKakaoAccessToken(code));
    }

}
