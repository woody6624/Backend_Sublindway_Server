package SublindWay_server.service;

import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import SublindWay_server.repository.TrainInfoRepository;
import SublindWay_server.utility.NearbySubwayInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionWithRealTimeServerService {
    public String setSubwayName;
    public List<NearbySubwayInfo> subwayInfos=new ArrayList<>();
    public List<NearbySubwayInfo> subwayInfos_next_station=new ArrayList<>();
    private final TrainInfoRepository trainInfoRepository;

    private SubwayDetailRepository subwayDetailRepository;
    @Autowired
    public ConnectionWithRealTimeServerService(TrainInfoRepository trainInfoRepository, SubwayDetailRepository subwayDetailRepository) {
        this.trainInfoRepository = trainInfoRepository;
        this.subwayDetailRepository=subwayDetailRepository;
    }

    public String connectionWithRealSubway(String requestStation,String upDown) {
        StringBuilder tempUrl=new StringBuilder("http://swopenAPI.seoul.go.kr/api/subway/7471724567776f6f37306753556564/json/realtimeStationArrival/0/20/");
        tempUrl.append(requestStation);
        String url=tempUrl.toString();
        WebClient webClient = WebClient.create();
        String responseBody = webClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parsing(responseBody,upDown);
    }
    public int checkRowNum(String subwayId) {
        switch(subwayId) {
            case "1001":
                return 1;
            case "1002":
                return 2;
            case "1003":
                return 3;
            case "1004":
                return 4;
            case "1005":
                return 5;
            case "1006":
                return 6;
            case "1007":
                return 7;
            case "1008":
                return 8;
            case "1009":
                return 9;
            default:
                return 999; // 존재하지 않는 호선 번호
        }
    }
    private List<NearbySubwayInfo> sortByBarvlDt(List<NearbySubwayInfo> subwayInfoList) {
        return subwayInfoList.stream()
                .sorted(Comparator.comparingInt(NearbySubwayInfo::getBarvlDt))
                .collect(Collectors.toList());
    }
    public String parsing(String responseBody,String upDown) {
        subwayInfos.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode realtimeArrivalList = rootNode.get("realtimeArrivalList");

            for (JsonNode node : realtimeArrivalList) {
                String subwayId=node.get("subwayId").asText();
                String statnFid = node.get("statnFid").asText();
                String statnId = node.get("statnId").asText();
                String statnTid = node.get("statnTid").asText();
                String arvlMsg2 = node.get("arvlMsg2").asText();
                String arvlMsg3 = node.get("arvlMsg3").asText();
                String arvlCd = node.get("arvlCd").asText();
                String barvlDt=node.get("barvlDt").asText();//도착 소요 시간정보(초)
                String updnLine=node.get("updnLine").asText();//상하행
                // 추출한 정보를 객체에 저장 또는 필요한 작업 수행
                NearbySubwayInfo subwayInfo = new NearbySubwayInfo();
                subwayInfo.setStatnFid(statnFid);
                subwayInfo.setStatnId(statnId);
                subwayInfo.setStatnTid(statnTid);
                subwayInfo.setArvlMsg2(arvlMsg2);
                subwayInfo.setArvlMsg3(arvlMsg3);
                subwayInfo.setUpdnLine(updnLine);
                int rowNum = 0;

                rowNum=checkRowNum(subwayId);//호선 체크
                subwayInfo.setArvlCd(arvlCd);
                int barvlDtInt = Integer.parseInt(barvlDt);
                subwayInfo.setBarvlDt(barvlDtInt);
                System.out.println(subwayInfo.getBarvlDt()+"초가 걸릴 예정입니다.\n");
                subwayInfos.add(subwayInfo);
            }

            List<NearbySubwayInfo> sortByBarvlDtData=sortByBarvlDt(subwayInfos);
            // 상행과 하행으로 분리
            List<NearbySubwayInfo> upLineInfos = sortByBarvlDtData.stream()
                    .filter(info -> "상행".equals(info.getUpdnLine()))
                    .collect(Collectors.toList());

            List<NearbySubwayInfo> downLineInfos = sortByBarvlDtData.stream()
                    .filter(info -> "하행".equals(info.getUpdnLine()))
                    .collect(Collectors.toList());

            // 상행 하행일떄 하나씩 선택(소요 시간이 최소인)
            NearbySubwayInfo nearestUpStation = upLineInfos.isEmpty() ? null : upLineInfos.get(0);//널체크
            NearbySubwayInfo nearestDownStation = downLineInfos.isEmpty() ? null : downLineInfos.get(0);//널체크
            if(upDown.equals("상행")){
                return upHill(nearestUpStation);
            }
            else if(upDown.equals("하행")){
                return downHill(nearestDownStation);
            }
            else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String upHill(NearbySubwayInfo nearbySubwayInfo){
        Optional<TrainInfoEntity> trainInfoEntity=trainInfoRepository.findById(nearbySubwayInfo.getBtrainNo());
        return trainInfoEntity.get().getStatnNm();
    }

    public String downHill(NearbySubwayInfo nearbySubwayInfo){
        Optional<TrainInfoEntity> trainInfoEntity=trainInfoRepository.findById(nearbySubwayInfo.getBtrainNo());
        return trainInfoEntity.get().getStatnNm();
    }


}
