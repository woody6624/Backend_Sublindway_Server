package SublindWay_server.repository;

import SublindWay_server.entity.SubwayDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayDetailRepository extends JpaRepository<SubwayDetailEntity,Integer> {

    @Query("SELECT s FROM SubwayDetailEntity s WHERE s.subwayPositionX = :locationX AND s.subwayPositionY = :locationY")
    SubwayDetailEntity findByLocationXY(double locationX, double locationY);


}
