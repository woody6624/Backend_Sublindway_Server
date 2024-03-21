package SublindWay_server.Dto;

import SublindWay_server.Domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserLocationDTO {
    private final Long userLocation_id;
    private final Long position_x;
    private final Long position_y;
    private final User user;

}
