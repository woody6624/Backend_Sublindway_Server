package SublindWay_server.Dao;

import SublindWay_server.Domain.SubwayRealTimeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayRealTimeDataRepository extends JpaRepository<SubwayRealTimeData, Long> {
}
