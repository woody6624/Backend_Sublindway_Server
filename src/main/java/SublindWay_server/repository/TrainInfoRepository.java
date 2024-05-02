package SublindWay_server.repository;

import SublindWay_server.entity.TrainInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainInfoRepository extends JpaRepository<TrainInfoEntity,String> {

}
