package SublindWay_server.Dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private final String user_id; // 기본키 유저 아이디
    private final String user_name; // 유저 이름
    private final String user_email; // 유저 이메일
    private final String user_pwd; // 유저 비밀번호
    private final String user_role; // 유저 정보 (일반유저, 운영자)
}
