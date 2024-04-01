package SublindWay_server.controller;

import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.service.SubwaySearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwayController {

    @Autowired
    private SubwaySearchService subwaySearchService;

    @GetMapping("/get-subway-name")
    @ApiOperation(value = "이거에요", notes = "이겁니다")

    public SubwayDetailDTO findSubwayByCoordinates(@RequestParam double locationX, @RequestParam double locationY) {
        return subwaySearchService.getSubwayName(locationX, locationY);
    }


}
