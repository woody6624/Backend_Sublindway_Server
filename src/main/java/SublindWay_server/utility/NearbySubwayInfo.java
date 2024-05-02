package SublindWay_server.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NearbySubwayInfo {
    @JsonProperty("statnFid")
    private String statnFid;
    @JsonProperty("statnId")
    private String statnId;
    @JsonProperty("statnTid")
    private String statnTid;
    @JsonProperty("arvlMsg2")
    private String arvlMsg2;
    @JsonProperty("arvlMsg3")
    private String arvlMsg3;
    @JsonProperty("arvlCd")
    private String arvlCd;
    @JsonProperty("updnLine") //상 하행 구분
    private String updnLine;
    @JsonProperty("btrainNo")
    private String btrainNo;


    private int barvlDt;
}
