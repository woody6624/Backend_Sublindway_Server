package SublindWay_server.Dto;

import SublindWay_server.Domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserCameraDataDTO {
    private final Long user_camera_data_id;
    private final User user;

}
