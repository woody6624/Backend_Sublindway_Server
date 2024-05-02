package SublindWay_server.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TrainInfo {
    @JsonProperty("statnNm")
    private String statnNm; // 현재 역 정보

    @JsonProperty("subwayNm")
    private String subwayNm; // 호선 정보

    @JsonProperty("trainNo")
    private String trainNo; // 열차 번호

    @JsonProperty("updnLine")
    private String updnLine; // 상하행 구분

    @JsonProperty("statnTnm")
    private String statnTnm; // 목적지 역 정보

    @JsonProperty("trainSttus")
    private String trainSttus; // 열차 상태 구분

    @JsonProperty("directAt")
    private String directAt; // 급행 여부

    @JsonProperty("lstcarAt")
    private String lstcarAt; // 막차 여부
}

