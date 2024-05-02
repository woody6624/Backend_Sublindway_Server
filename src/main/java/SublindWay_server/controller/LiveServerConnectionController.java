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
    @GetMapping("/")
    @ApiOperation(value = "역넣기", notes = "subwayId를 넣으면 ")
    public String tests(@RequestParam String subwayId,@RequestParam String upDown){
        return connectionWithRealTimeServerSerive.connectionWithRealSubway(subwayId,upDown);
    }
}
