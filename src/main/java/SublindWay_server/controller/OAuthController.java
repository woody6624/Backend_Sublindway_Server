package SublindWay_server.controller;

import SublindWay_server.service.OAuthService;
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
    public String kakaoCallback(@RequestParam String code){
        String gugucaca=oAuthService.getKakaoAccessToken(code);
        System.out.println(gugucaca);
        return oAuthService.createKakaoUser(gugucaca);
    }


}
