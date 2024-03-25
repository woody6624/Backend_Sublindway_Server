package SublindWay_server.Dao;

import SublindWay_server.Domain.SubwayData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayDataRepository extends JpaRepository<SubwayData,Long> {



}
