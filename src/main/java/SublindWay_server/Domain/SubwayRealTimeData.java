package SublindWay_server.Domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SubwayRealTimeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long realTimeData_id;
    private String whereToGo;

    // 연관관계 -> 지하철데이터
    @OneToOne
    @JoinColumn(name="subwayData_id")
    private SubwayData subwayData;

    @Builder
    public SubwayRealTimeData(Long realTimeData_id,String whereToGo){
        this.realTimeData_id=realTimeData_id;
        this.whereToGo=whereToGo;
    }


}
