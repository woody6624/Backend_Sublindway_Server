package SublindWay_server.controller;

import SublindWay_server.dto.SendWebData;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.dto.SendXyLocation;
import SublindWay_server.service.SubwaySearchService;
import SublindWay_server.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SubwayController {

    @Autowired
    private SubwaySearchService subwaySearchService;

    @Autowired
    private SseService sseService; // SSE 서비스 주입

    @GetMapping("/subway-name-by-location")
    @Operation(summary = "지하철역 이름 얻기", description = "본인의 xy좌표와 사용자 ID를 통하여 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEuclid(
            @Parameter(description = "사용자 ID", required = true) @RequestParam String userId,
            @Parameter(description = "x 좌표(소수점 6자리)", required = true) @RequestParam double locationX,
            @Parameter(description = "y 좌표(소수점 6자리)", required = true) @RequestParam double locationY) {

        SubwayDetailDTO subwayDetail = subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);

        // SendXyLocation 객체를 SendWebData로 래핑
        SendWebData sendWebData = new SendWebData("", "", locationX, locationY);
        sseService.updateLastKnownLocation(userId, sendWebData); // 위치 데이터 업데이트
        sseService.sendEventToUser(userId, sendWebData); // 클라이언트에게 SSE 이벤트 전송

        return subwayDetail;
    }

    @GetMapping("/stream/subway/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        return sseService.subscribe(userId); // SSE 구독
    }
}
