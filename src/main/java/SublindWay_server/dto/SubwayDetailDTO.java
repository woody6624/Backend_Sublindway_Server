package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SubwayDetailDTO {
    private Integer subwayNum; // 지하철 호선 번호
    private String subwayName; // 지하철 역 이름

}
