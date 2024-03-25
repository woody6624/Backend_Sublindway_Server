package SublindWay_server.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CameraCheckRepository extends JpaRepository<CameraCheckRepository,Long> {

    // select 열 이름 from 테이블 이름 where 조건식
}
