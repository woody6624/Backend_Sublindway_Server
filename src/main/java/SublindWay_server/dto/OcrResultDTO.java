package SublindWay_server.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OcrResultDTO {
    private List<String> ocrResult;
}
