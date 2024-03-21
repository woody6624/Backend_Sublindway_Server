package SublindWay_server.Domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CameraCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_camera_data_id;

    // 연관관계 -> 음성
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="voice_id")
    private Voice voice;

    // 연관관계 -> 유저 카메라 데이터
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_camera_data_id")
    private UserCameraData userCameraData;

    @Builder
    public CameraCheck(Long user_camera_data_id,UserCameraData userCameraData,Voice voice){
        this.user_camera_data_id=user_camera_data_id;
        this.userCameraData=userCameraData;
        this.voice=voice;
    }

}
