package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
public class SendImagesUuidDTO {
    private String imageUUID;

    private String muckatUserId;

    private String userName;

    private LocalDateTime localDateTime;

    private String yoloOrRide;
}
