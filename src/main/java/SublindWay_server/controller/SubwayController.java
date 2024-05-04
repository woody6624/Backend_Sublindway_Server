package SublindWay_server.controller;

import SublindWay_server.dto.SendXyLocation;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.service.SubwaySearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    @ApiOperation(value = "지하철역 이름 얻기", notes = "본인의 xy좌표를 통하여서 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEuclid(@RequestParam double locationX, @RequestParam double locationY) {
        SubwayDetailDTO subwayDetail = subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
        // 좌표를 웹 클라이언트에 실시간으로 전송
        SendXyLocation sendXyLocation = new SendXyLocation(locationX, locationY);
        messagingTemplate.convertAndSend("/topic/subwayLocation", sendXyLocation);
        return subwayDetail;
    }
}
