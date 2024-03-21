package SublindWay_server.Dto;


import SublindWay_server.Domain.UserLocation;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SubwayDataDTO {
    private final Long subwayData_id;
    private final Long location_id;
    private final String line_name;
    private final String subway_name;
    private final UserLocation userLocation;

}
