package SublindWay_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "train_info")
@Getter
@Setter
@RequiredArgsConstructor
public class TrainInfoEntity {
    @Id
    @Column(name="trainNo")
    private String trainNo; // 열차 번호

    @Column(name = "statnNm")
    private String statnNm; // 현재 역 정보

    @Column(name = "subwayNm")
    private String subwayNm; // 호선 정보

    @Column(name = "updnLine")
    private String updnLine; // 상하행 구분

    @Column(name = "statnTnm")
    private String statnTnm; // 목적지 역 정보

    @Column(name = "trainSttus")
    private String trainSttus; // 열차 상태 구분

    @Column(name = "directAt")
    private String directAt; // 급행 여부

    @Column(name = "lstcarAt")
    private String lstcarAt; // 막차 여부

    @Column(name="statnId") //역이름
    private String statnId;
}
