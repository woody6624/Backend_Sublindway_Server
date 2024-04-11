package SublindWay_server.service;

import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.repository.SubwayDetailRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionWithRealTimeServerService {
    public String setSubwayName;
    public List<NearbySubwayInfo> subwayInfos=new ArrayList<>();

    private SubwayDetailRepository subwayDetailRepository;
    @Autowired
    public ConnectionWithRealTimeServerService(SubwayDetailRepository subwayDetailRepository) {
        this.subwayDetailRepository=subwayDetailRepository;
    }

    public void connectionWithRealSubway(String requestStation) {
        StringBuilder tempUrl=new StringBuilder("http://swopenAPI.seoul.go.kr/api/subway/7471724567776f6f37306753556564/json/realtimeStationArrival/0/5/");
        tempUrl.append(requestStation);
        String url=tempUrl.toString();
        WebClient webClient = WebClient.create();
        String responseBody = webClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        parsing(responseBody);
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

    public void parsing(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode realtimeArrivalList = rootNode.get("realtimeArrivalList");

            for (JsonNode node : realtimeArrivalList) {
                String subwayId=node.get("subwayId").asText();
                String statnFid = node.get("statnFid").asText();
                String statnNm=node.get("statnNm").asText();
                String statnId = node.get("statnId").asText();
                String statnTid = node.get("statnTid").asText();
                String arvlMsg2 = node.get("arvlMsg2").asText();
                String arvlMsg3 = node.get("arvlMsg3").asText();
                String arvlCd = node.get("arvlCd").asText();
                String barvlDt=node.get("barvlDt").asText();
                // 추출한 정보를 객체에 저장 또는 필요한 작업 수행
                NearbySubwayInfo subwayInfo = new NearbySubwayInfo();
                subwayInfo.setStatnFid(statnFid);
                subwayInfo.setStatnId(statnId);
                subwayInfo.setStatnTid(statnTid);
                subwayInfo.setArvlMsg2(arvlMsg2);
                subwayInfo.setArvlMsg3(arvlMsg3);

                int rowNum = 0;

                rowNum=checkRowNum(subwayId);//비교

                SubwayDetailEntity subwayDetailEntity=subwayDetailRepository.findSubwayIdBySubwayNameAndRowNum(arvlMsg3,rowNum);
                int currentStation=subwayDetailEntity.getSubwayNum();//현재 열차가 위치할 예정인 역

                SubwayDetailEntity subwayDetailEntity2=subwayDetailRepository.findSubwayIdBySubwayNameAndRowNum(statnNm,rowNum);
                int targetStation=subwayDetailEntity2.getSubwayNum();//현재 도착해야되는(내가 있는)역
                int requiredTime = Math.abs(currentStation-targetStation)*180;
                int barvlDtInt = Integer.parseInt(barvlDt);
                barvlDtInt = barvlDtInt + requiredTime;

                if((currentStation-targetStation)<0){
                    Optional<SubwayDetailEntity> subwayDetailEntity3=subwayDetailRepository.findById(targetStation+1);
                    System.out.println(subwayDetailEntity3.get().getSubwayName()+"방향으로 가는 지하철입니다..\n");

                }

                else{
                    Optional<SubwayDetailEntity> subwayDetailEntity3=subwayDetailRepository.findById(targetStation-1);
                    System.out.println(subwayDetailEntity3.get().getSubwayName()+"방향으로 가는 지하철입니다..\n");

                }
                //만약에 서로의 역 id값이 음수 혹은 양수라면...

                subwayInfo.setArvlCd(arvlCd);
                subwayInfo.setBarvlDt(barvlDtInt);
                System.out.println(subwayInfo.getBarvlDt()+"초가 걸릴 예정입니다.\n");
                subwayInfos.add(subwayInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
