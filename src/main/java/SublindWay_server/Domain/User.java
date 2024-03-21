package SublindWay_server.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String user_id; // 기본키 유저 아이디
    private String user_name; // 유저 이름
    private String user_email; // 유저 이메일
    private String user_pwd; // 유저 비밀번호
    private String user_role; // 유저 정보 (일반유저, 운영자)

    @Builder
    public User(String user_id,String user_name, String user_email, String user_pwd, String user_role){
        this.user_id=user_id;
        this.user_name=user_name;
        this.user_email=user_email;
        this.user_pwd=user_pwd;
        this.user_role=user_role;
    }
}
