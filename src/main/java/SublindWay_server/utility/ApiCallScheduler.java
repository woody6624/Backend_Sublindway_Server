/*package SublindWay_server.utility;

import SublindWay_server.service.GetAllSubwayTrainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiCallScheduler {
    private final GetAllSubwayTrainDataService getAllSubwayTrainDataService;

    @Autowired
    public ApiCallScheduler(GetAllSubwayTrainDataService getAllSubwayTrainDataService) {
        this.getAllSubwayTrainDataService = getAllSubwayTrainDataService;
    }
    @Scheduled(fixedRate = 60000) // 60초마다 실행
    public void myScheduledFunction() {
        getAllSubwayTrainDataService.getAllTrainData();
    }
}
*/