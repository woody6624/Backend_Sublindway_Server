package SublindWay_server.controller;

import SublindWay_server.dto.SendXyLocation;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.service.SubwaySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwayController {

    @Autowired
    private SubwaySearchService subwaySearchService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/subway-name-by-location")
    @Operation(summary = "지하철역 이름 얻기", description = "본인의 xy좌표를 통하여서 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEuclid(@Parameter(description = "x 좌표(소수점 6자리)", required = true) @RequestParam double locationX,
                                              @Parameter(description = "y 좌표(소수점 6자리)", required = true) @RequestParam double locationY) {
        SubwayDetailDTO subwayDetail = subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
        // 좌표를 웹 클라이언트에 실시간으로 전송
        SendXyLocation sendXyLocation = new SendXyLocation(locationX, locationY);
        messagingTemplate.convertAndSend("/topic/subwayLocation", sendXyLocation);
        return subwayDetail;
    }
}
