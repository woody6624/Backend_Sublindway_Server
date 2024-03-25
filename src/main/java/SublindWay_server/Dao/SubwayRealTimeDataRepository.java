package SublindWay_server.Dao;

import SublindWay_server.Domain.SubwayRealTimeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayRealTimeDataRepository extends JpaRepository<SubwayRealTimeData, Long> {
}
