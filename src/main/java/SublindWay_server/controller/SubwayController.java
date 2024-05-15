package SublindWay_server.controller;

import SublindWay_server.dto.SendXyLocation;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.service.SubwaySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SubwayController {
    @Autowired
    private SubwaySearchService subwaySearchService;

    @GetMapping("/subway-name-by-location")
    @Operation(summary = "지하철역 이름 얻기", description = "본인의 xy좌표와 사용자 ID를 통하여 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEuclid(
            @Parameter(description = "사용자 ID", required = true) @RequestParam String userId,
            @Parameter(description = "x 좌표(소수점 6자리)", required = true) @RequestParam double locationX,
            @Parameter(description = "y 좌표(소수점 6자리)", required = true) @RequestParam double locationY) {

        SubwayDetailDTO subwayDetail = subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
        SendXyLocation sendXyLocation = new SendXyLocation(locationX, locationY);
        return subwayDetail;
    }

}
