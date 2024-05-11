package SublindWay_server.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "kakao_id")
    private String muckatUserId;

    @Column(name = "user_name")
    private String userName;
}
