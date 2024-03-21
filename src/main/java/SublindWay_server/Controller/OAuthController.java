package SublindWay_server.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    @GetMapping("/login")
    public String home(){
        return "login";
    }

    @GetMapping("/user")
    public String userPage(){
        return "userPage";
    }

    @GetMapping("/oauth/kakao")
    public @ResponseBody String kakaoCallback(String code){
        return "카카오 서버로부터 받은 CODE 정보: "+code;
    }
}
