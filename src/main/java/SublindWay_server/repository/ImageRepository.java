package SublindWay_server.repository;

import SublindWay_server.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,String> {
    @Query("SELECT u FROM ImageEntity u WHERE u.userEntity.muckatUserId = :kakaoId")
    List<ImageEntity> findByKakaoId(String kakaoId);

}
