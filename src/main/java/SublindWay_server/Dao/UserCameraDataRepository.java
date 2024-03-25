package SublindWay_server.Dao;

import SublindWay_server.Domain.UserCameraData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCameraDataRepository extends JpaRepository<UserCameraData,Long> {
}
