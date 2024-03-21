package SublindWay_server.Domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserCameraData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_camera_data_id;

    // 연관관계 -> 유저
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public UserCameraData(Long user_camera_data_id){
        this.user_camera_data_id=user_camera_data_id;
    }

}
