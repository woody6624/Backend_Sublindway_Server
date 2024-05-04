package SublindWay_server.controller;

import SublindWay_server.service.ConnectionWithRealTimeServerService;
import SublindWay_server.utility.NearbySubwayInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value="/live-subway")
public class LiveServerConnectionController {
    @Autowired
    ConnectionWithRealTimeServerService connectionWithRealTimeServerSerive;
    @GetMapping("/station-updown")
    @ApiOperation(value = "열차 번호 얻기", notes = "열차역 이름과 상하행 2개를 넣어서 탑승할 열차번호 얻기")
    public String tests(@RequestParam String subwayId,@RequestParam String upDown){
        return connectionWithRealTimeServerSerive.connectionWithRealSubway(subwayId,upDown);
    }
    @GetMapping("/track-train")
    @ApiOperation(value = "열차추적", notes = "열차번호로 역이 어딘지 추적")
    public String trackingTrain(@RequestParam String trainNumber){
        return connectionWithRealTimeServerSerive.trackingTrain(trainNumber);
    }
}
