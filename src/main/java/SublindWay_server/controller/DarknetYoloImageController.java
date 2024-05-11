package SublindWay_server.controller;

import SublindWay_server.service.DarknetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class DarknetYoloImageController {

    @Autowired
    private DarknetService darknetService;

    @GetMapping("/analyze-image")
    public List<String> analyzeImage(@RequestParam String imagePath) {
        return darknetService.runDarknet(imagePath);
    }
}
