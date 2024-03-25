package SublindWay_server.Dao;

import SublindWay_server.Domain.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRepository extends JpaRepository <Voice, Long> {
}
