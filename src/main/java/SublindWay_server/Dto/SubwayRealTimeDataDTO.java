package SublindWay_server.Dto;

import SublindWay_server.Domain.SubwayData;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SubwayRealTimeDataDTO {
    private final Long realTimeData_id;
    private final String whereToGo;
    private final SubwayData subwayData;

}
