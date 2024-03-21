package SublindWay_server.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLocation_id;
    private Long position_x;
    private Long position_y;

    // 연관관계 -> 유저
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public UserLocation(Long userLocation_id, Long position_x, Long position_y){
        this.userLocation_id=userLocation_id;
        this.position_x=position_x;
        this.position_y=position_y;
    }
}
