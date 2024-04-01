package SublindWay_server.repository;

import SublindWay_server.entity.OcrResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResultEntity,Long> {
}
