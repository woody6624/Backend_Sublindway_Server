package SublindWay_server.controller;
import SublindWay_server.dto.SendXyLocation;
import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.service.SubwaySearchService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SubwayController {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    private SubwaySearchService subwaySearchService;

    @GetMapping("/subway-name-by-location")
    @Operation(summary = "지하철역 이름 얻기", description = "본인의 xy좌표와 사용자 ID를 통하여 근처 지하철 역의 이름을 얻을 수 있습니다.")
    public SubwayDetailDTO findSubwayByEuclid(
            @Parameter(description = "사용자 ID", required = true) @RequestParam String userId,
            @Parameter(description = "x 좌표(소수점 6자리)", required = true) @RequestParam double locationX,
            @Parameter(description = "y 좌표(소수점 6자리)", required = true) @RequestParam double locationY) {

        SubwayDetailDTO subwayDetail = subwaySearchService.getSubwayDetailsByLocation(locationX, locationY);
        sendEventToUser(userId, new SendXyLocation(locationX, locationY));

        return subwayDetail;
    }

    @GetMapping("/stream/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    private void sendEventToUser(String userId, SendXyLocation sendXyLocation) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(sendXyLocation, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
