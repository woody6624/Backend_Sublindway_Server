package SublindWay_server.Dao;

import SublindWay_server.Domain.SubwayData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubwayDataRepository extends JpaRepository<SubwayData,Long> {

//    @Query("SELECT u FROM User u WHERE u.username = :username")
//    User findByUsername(@Param("username") String username);

}
