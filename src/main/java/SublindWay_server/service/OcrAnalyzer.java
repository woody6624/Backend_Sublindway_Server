package SublindWay_server.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OcrAnalyzer {

    @Autowired
    private SubwayUpDownCheckService subwayService;

    public List<String> getOcrSubwayNumList(String ocrResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode getAllJsonData = objectMapper.readTree(ocrResult);
        JsonNode imageArrJsonData = getAllJsonData.get("images");
        List<Integer> subwayNums = new ArrayList<>();
        List<String> answerList = new ArrayList<>();

        for (JsonNode imageData : imageArrJsonData) {
            JsonNode fieldsDatas = imageData.get("fields");
            for (JsonNode fieldData : fieldsDatas) {
                JsonNode inferTextNode = fieldData.get("inferText");
                if (inferTextNode != null) {
                    String inferText = inferTextNode.asText();
                    if (isNumber(inferText) && !inferText.contains("-")) {
                        subwayNums.add(Integer.parseInt(inferText));
                        System.out.println(inferText);
                    }
                }
            }
        }

        if (subwayNums.isEmpty()) {
            return getOcrSubwayRangeList(ocrResult);
        }

        if (subwayNums.size() < 2) {
            answerList.add("최소한 2개 이상의 숫자를 찍어야 합니다.");
            return answerList;
        }

        int start = subwayNums.get(0);
        int end = subwayNums.get(1);

        String direction = subwayService.findDirection(start, end);
        answerList.add(direction);

        return answerList;
    }

    public List<String> getOcrSubwayRangeList(String ocrResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode getAllJsonData = objectMapper.readTree(ocrResult);
        JsonNode imageArrJsonData = getAllJsonData.get("images");
        List<String> hyphenNum = new ArrayList<>();

        for (JsonNode imageData : imageArrJsonData) {
            JsonNode fieldsDatas = imageData.get("fields");
            for (JsonNode fieldData : fieldsDatas) {
                JsonNode inferTextNode = fieldData.get("inferText");
                if (inferTextNode != null) {
                    String inferText = inferTextNode.asText();
                    if (isHyphenNumber(inferText)) {
                        hyphenNum.add(inferText);
                        System.out.println(inferText);
                    }
                }
            }
        }

        if (hyphenNum.isEmpty()) {
            hyphenNum.add("적절한 이미지가 아닙니다. 다시 찍어주세요.");
        }

        return hyphenNum;
    }

    private static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean isHyphenNumber(String str) {
        return str.matches("-?\\d+-\\d+");
    }
}
