package SublindWay_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OcrAnalyzer {

    public List<String> getOcrSubwayNumList(String ocrResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode getAllJsonData = objectMapper.readTree(ocrResult);
        JsonNode imageArrJsonData = getAllJsonData.get("images");
        List<String> subwayNums = new ArrayList<>();
        for (JsonNode imageData : imageArrJsonData) {
            JsonNode fieldsDatas = imageData.get("fields");
            for (JsonNode fieldData : fieldsDatas) {
                JsonNode inferTextNode = fieldData.get("inferText");
                if (inferTextNode != null) {
                    String inferText = inferTextNode.asText();
                    // 숫자인 경우 '-'가 없는지 확인하여 숫자 리스트에 추가
                    if (isNumber(inferText) && !inferText.contains("-")) {
                        subwayNums.add(inferText);
                        System.out.println(inferText);
                    }
                }
            }
        }
        return subwayNums;
    }

    private static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
