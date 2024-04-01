package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserLocationDTO {
    private Long locationId; // 위치 ID
    private String kakaoId; // 사용자의 카카오 ID
    private Integer subwayNumber; // 지하철 호선 번호
    private double positionX; // 위치의 X 좌표
    private double positionY; // 위치의 Y 좌표
}
