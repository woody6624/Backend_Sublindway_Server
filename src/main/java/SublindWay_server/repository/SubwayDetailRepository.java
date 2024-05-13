package SublindWay_server.repository;

import SublindWay_server.entity.SubwayDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayDetailRepository extends JpaRepository<SubwayDetailEntity,Integer> {

    @Query("SELECT s FROM SubwayDetailEntity s WHERE s.subwayPositionX = :locationX AND s.subwayPositionY = :locationY")
    SubwayDetailEntity findByLocationXY(double locationX, double locationY);

    @Query("SELECT s FROM SubwayDetailEntity s WHERE s.subwayLine = :subwayLine AND s.subwayName = :subwayName")
    SubwayDetailEntity findSubwayIdBySubwayNameAndRowNum(String subwayName, int subwayLine);

    @Query(value = "SELECT * FROM subway_detail ORDER BY ST_Distance_Sphere(point(:locationX, :locationY), point(subway_position_x, subway_position_y)) ASC LIMIT 1", nativeQuery = true)
    SubwayDetailEntity findNearestSubway(@Param("locationX") double locationY, @Param("locationY") double locationX);
    @Query("SELECT s FROM SubwayDetailEntity s WHERE s.subwayName = :subwayName AND s.subwayLine = :subwayLine")
    SubwayDetailEntity findSubwayBySubwayName(@Param("subwayName") String subwayName,@Param("subwayLine") String subwayLine);


}
