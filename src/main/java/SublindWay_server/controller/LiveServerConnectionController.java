package SublindWay_server.controller;

import SublindWay_server.entity.TrainInfoEntity;
import SublindWay_server.service.ConnectionWithRealTimeServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="/live-subway")
public class LiveServerConnectionController {
    @Autowired
    ConnectionWithRealTimeServerService connectionWithRealTimeServerService;

    @GetMapping("/station-updown")
    @Operation(summary = "열차 번호 얻기", description = "열차역 이름과 상하행 2개를 넣어서 탑승할 열차번호 얻기")
    public String tests(@Parameter(description = "역명(단 xx역이면 xx까지만 작성)", required = true) @RequestParam String subwayId,
                        @Parameter(description = "상행 혹은 하행 작성", required = true) @RequestParam String upDown) {

        return connectionWithRealTimeServerService.connectionWithRealSubway(subwayId, upDown);
    }

    @GetMapping("/track-train")
    @Operation(summary = "열차추적", description = "열차번호로 역이 어딘지 추적")
    public TrainInfoEntity trackingTrain(@Parameter(description = "열차번호", required = true) @RequestParam String trainNumber) {
        return connectionWithRealTimeServerService.trackingTrain(trainNumber);
    }
}
