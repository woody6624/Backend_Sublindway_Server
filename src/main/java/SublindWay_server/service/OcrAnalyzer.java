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
        List<String> answerList = new ArrayList<>();
        for (JsonNode imageData : imageArrJsonData) {
            JsonNode fieldsDatas = imageData.get("fields");
            for (JsonNode fieldData : fieldsDatas) {
                JsonNode inferTextNode = fieldData.get("inferText");
                if (inferTextNode != null) {
                    String inferText = inferTextNode.asText();
                    if (isNumber(inferText) && !inferText.contains("-")) {
                        subwayNums.add(inferText);
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
        int difference = Integer.parseInt(subwayNums.get(0)) - Integer.parseInt(subwayNums.get(1));
        if (difference > 0) {
            answerList.add("하행");
        } else if (difference < 0) {
            answerList.add("상행");
        } else {
            answerList.add("다시 사진을 찍어 주세요.");  // 비교 불가능한 상황일 때 메시지
        }
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
