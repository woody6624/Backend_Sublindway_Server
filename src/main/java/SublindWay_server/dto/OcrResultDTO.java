package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OcrResultDTO {
    private Long resultId;
    private String imageUUID; // 이미지 식별자
    private Boolean upDown; // 오르막인지 내리막인지 여부
    private Integer firstNum; // 첫 번째 지하철 호선 번호
    private Integer secondNum; // 두 번째 지하철 호선 번호
    private Integer thirdNum; // 세 번째 지하철 호선 번호

}
