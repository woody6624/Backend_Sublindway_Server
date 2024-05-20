package SublindWay_server.controller;

import SublindWay_server.repository.UserRepository;
import SublindWay_server.service.MobileLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="/moblie")
public class MobileLoginController {
    @Autowired
    MobileLoginService mobileLoginService;

    @GetMapping("/return-kakaoId")
    @Operation(summary = "모바일 로그인 시도", description = "카카오 id값 반환")
    public String tests(@Parameter(description = "카카오 Id를 주세용", required = true) @RequestParam String kakaoId,
                        @Parameter(description = "유저이름을 주세용", required = true) @RequestParam String userName) {
        return mobileLoginService.getKakaoId(kakaoId,userName);
    }
}
