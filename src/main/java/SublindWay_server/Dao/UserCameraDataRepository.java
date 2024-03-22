package SublindWay_server.Dao;

import SublindWay_server.Domain.UserCameraData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCameraDataRepository extends JpaRepository<UserCameraData,Long> {
}
