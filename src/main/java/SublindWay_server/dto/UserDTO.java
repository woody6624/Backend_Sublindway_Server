package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private String muckatUserId; // 카카오 ID
    private String userName; // 사용자 이름

}
