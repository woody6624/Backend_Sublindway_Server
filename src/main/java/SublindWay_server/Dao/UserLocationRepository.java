package SublindWay_server.Dao;

import SublindWay_server.Domain.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLocationRepository extends JpaRepository<UserLocation,Long> {


}
