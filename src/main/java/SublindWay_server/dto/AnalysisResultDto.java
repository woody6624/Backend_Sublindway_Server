package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AnalysisResultDto {
    private String extractedText;
    private String analysisSummary;
    // 생성자, 게터, 세터 생략
}