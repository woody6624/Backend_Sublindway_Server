package SublindWay_server.repository;

import SublindWay_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.accessToken = :accessToken")
    Optional<UserEntity> findByAccessToken(@Param("accessToken") String accessToken);
}
