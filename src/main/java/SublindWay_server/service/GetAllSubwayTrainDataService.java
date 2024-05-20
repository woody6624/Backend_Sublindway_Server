package SublindWay_server.service;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import SublindWay_server.repository.TrainInfoRepository;
import SublindWay_server.utility.TrainInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GetAllSubwayTrainDataService {


    private final TrainInfoRepository trainInfoRepository;
    private  final SubwayDetailRepository subwayDetailRepository;
    @Autowired
    public GetAllSubwayTrainDataService(TrainInfoRepository trainInfoRepository, SubwayDetailRepository subwayDetailRepository){
        this.trainInfoRepository=trainInfoRepository;
        this.subwayDetailRepository = subwayDetailRepository;
    }
    public void getAllTrainData(){ //모든 호선 데이터에 대해서
        trainInfoRepository.deleteAll();//레포지터리에 저장된 데이터를 모두 제거해줍니다.
        connectionWithRealSubway("1호선");
       // connectionWithRealSubway("2호선");
      //  connectionWithRealSubway("3호선");
        //connectionWithRealSubway("4호선");
      //  connectionWithRealSubway("5호선");
      //  connectionWithRealSubway("6호선");
      //  connectionWithRealSubway("7호선");
      //  connectionWithRealSubway("8호선");
      //  connectionWithRealSubway("9호선");
    }

    public void connectionWithRealSubway(String requestStation) {
        StringBuilder tempUrl=new StringBuilder("http://swopenapi.seoul.go.kr/api/subway/7471724567776f6f37306753556564/json/realtimePosition/0/100/");
        tempUrl.append(requestStation);
        String url=tempUrl.toString();
        System.out.println("정상실행");
        WebClient webClient = WebClient.create();
        String responseBody = webClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        parsingAndSave(responseBody);
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

    public void parsingAndSave(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode realtimeArrivalList = rootNode.get("realtimePositionList");
            if (realtimeArrivalList == null || !realtimeArrivalList.isArray()) {
                System.out.println("파싱 에러!");
                return;
            }
            for (JsonNode node : realtimeArrivalList) {
                TrainInfoEntity trainInfoEntity = new TrainInfoEntity();
                TrainInfo trainInfo=new TrainInfo();
                String trainNo = node.get("trainNo").asText();
                String statnNm = node.get("statnNm").asText();
                String subwayNm = node.get("subwayNm").asText();
                String updnLine = node.get("updnLine").asText();
                String statnTnm = node.get("statnTnm").asText();
                String trainSttus = node.get("trainSttus").asText();
                String directAt = node.get("directAt").asText();
                String lstcarAt = node.get("lstcarAt").asText();
                String statnId = node.get("statnId").asText();
                trainInfo.setTrainNo(trainNo);
                trainInfo.setStatnNm(statnNm);
                trainInfo.setSubwayNm(subwayNm);
                trainInfo.setUpdnLine(updnLine);
                trainInfo.setStatnTnm(statnTnm);
                trainInfo.setTrainSttus(trainSttus);
                trainInfo.setDirectAt(directAt);
                trainInfo.setLstcarAt(lstcarAt);
                trainInfoEntity.setTrainNo(trainInfo.getTrainNo());
                trainInfoEntity.setStatnNm(trainInfo.getStatnNm());
                trainInfoEntity.setSubwayNm(trainInfo.getSubwayNm());
                trainInfoEntity.setUpdnLine(trainInfo.getUpdnLine());
                trainInfoEntity.setStatnTnm(trainInfo.getStatnTnm());
                trainInfoEntity.setTrainSttus(trainInfo.getTrainSttus());
                trainInfoEntity.setDirectAt(trainInfo.getDirectAt());
                trainInfoEntity.setLstcarAt(trainInfo.getLstcarAt());
                trainInfoEntity.setStatnId(statnId);
                System.out.println(trainInfoEntity.toString());
                trainInfoRepository.saveAndFlush(trainInfoEntity);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
