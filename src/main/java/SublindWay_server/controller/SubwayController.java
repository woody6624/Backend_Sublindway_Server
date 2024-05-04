package SublindWay_server.controller;

import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.service.SubwaySearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwayController {

    @Autowired
    private SubwaySearchService subwaySearchService;
    @GetMapping("/subway-name-by-location")
    @ApiOperation(value = "지하철역 이름 얻기", notes = "본인의 xy좌표를 통하여서 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEUclid(@RequestParam double locationX, @RequestParam double locationY) {
        return subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
    }

    @MessageMapping("/sendLocation")
    @SendTo("/topic/subwayLocation")
    public SubwayDetailDTO sendSubwayLocation(double locationX, double locationY) {
        // 실시간으로 좌표 데이터를 웹 클라이언트에 전송
        return subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
    }
}
