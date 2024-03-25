package SublindWay_server.Domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserCameraData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_camera_data_id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public UserCameraData(Long user_camera_data_id){
        this.user_camera_data_id=user_camera_data_id;
    }

}
