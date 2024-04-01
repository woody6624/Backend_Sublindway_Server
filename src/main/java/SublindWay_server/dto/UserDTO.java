package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private String muckatUserId; // 카카오 ID
    private String userName; // 사용자 이름
    private String email; // 이메일
    private String refreshToken; // 리프레시 토큰

}
