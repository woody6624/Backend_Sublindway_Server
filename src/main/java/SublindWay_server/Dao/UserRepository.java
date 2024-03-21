package SublindWay_server.Dao;

import SublindWay_server.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByOauth2Id(String oauth2Id);
    public Optional<User> findByName(String username);


}
