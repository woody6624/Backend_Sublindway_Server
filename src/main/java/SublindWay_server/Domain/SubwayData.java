package SublindWay_server.Domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SubwayData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subwayData_id;
    private Long location_id;
    private String line_name;
    private String subway_name;

    // 연관관계 -> 유저위치
    @ManyToOne
    @JoinColumn(name="userlocation_id")
    private UserLocation userLocation;

    @Builder
    public SubwayData(Long subwayData_id,Long location_id, String line_name, String subway_name){
        this.subwayData_id=subwayData_id;
        this.location_id=location_id;
        this.line_name=line_name;
        this.subway_name=subway_name;
    }
}
