package SublindWay_server.utility;

import SublindWay_server.service.GetAllSubwayTrainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class ApiCallScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ApiCallScheduler.class);
    private final GetAllSubwayTrainDataService getAllSubwayTrainDataService;

    @Autowired
    public ApiCallScheduler(GetAllSubwayTrainDataService getAllSubwayTrainDataService) {
        this.getAllSubwayTrainDataService = getAllSubwayTrainDataService;
    }

    @Scheduled(fixedRate = 180000) // 1분마다 실행
    public void myScheduledFunction() {
        try {
            getAllSubwayTrainDataService.getAllTrainData();
        } catch (Exception e) {
            logger.error("Unexpected error occurred in scheduled task", e);
        }
    }
}
