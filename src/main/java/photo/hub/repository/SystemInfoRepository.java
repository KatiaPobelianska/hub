package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photo.hub.model.SystemInfo;

import java.util.List;
import java.util.Optional;

public interface SystemInfoRepository extends JpaRepository<SystemInfo, Long> {
    Optional<SystemInfo> findByTitle(String title);

}
