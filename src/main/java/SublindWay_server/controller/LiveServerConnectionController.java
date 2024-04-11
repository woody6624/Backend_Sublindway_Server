package SublindWay_server.controller;

import SublindWay_server.service.ConnectionWithRealTimeServerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class LiveServerConnectionController {
    @Autowired
    ConnectionWithRealTimeServerService connectionWithRealTimeServerSerive;
    @GetMapping("/test")
    @ApiOperation(value = "역넣기", notes = "subwayId를 넣으면 ")
    public void tests(@RequestParam String subwayId){
        connectionWithRealTimeServerSerive.connectionWithRealSubway(subwayId);
    }
}
