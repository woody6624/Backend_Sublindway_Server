package SublindWay_server.Dao;

import SublindWay_server.Domain.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation,Long> {
}
