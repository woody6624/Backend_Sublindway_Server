package SublindWay_server.service;

import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class SubwayUpDownCheckService {
    @Autowired
    private SubwayDetailRepository subwayDetailRepository;

    public String findDirection(int startId, int endId) {
        Optional<SubwayDetailEntity> startStationOpt = subwayDetailRepository.findById(startId);
        Optional<SubwayDetailEntity> endStationOpt = subwayDetailRepository.findById(endId);

        if (startStationOpt.isPresent() && endStationOpt.isPresent()) {
            SubwayDetailEntity startStation = startStationOpt.get();
            SubwayDetailEntity endStation = endStationOpt.get();

            String line = startStation.getSubwayLine();
            if (!line.equals(endStation.getSubwayLine())) {
                return "출발역과 도착역이 다른 호선에 있습니다.";
            }

            int startNumber = startStation.getSubwayNum();
            int endNumber = endStation.getSubwayNum();

            return getDirection(line, startNumber, endNumber);
        } else {
            return "출발역 또는 도착역을 찾을 수 없습니다.";
        }
    }

    private String getDirection(String line, int start, int end) {
        switch (line) {
            case "01호선":
            case "04호선":
                return (start < end) ? "상행" : "하행";
            case "03호선":
            case "05호선":
            case "06호선":
            case "07호선":
            case "08호선":
            case "09호선":
                return (start < end) ? "하행" : "상행";
            default:
                return "알 수 없는 호선입니다.";
        }
    }
}
