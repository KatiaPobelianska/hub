package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import photo.hub.model.Post;
import photo.hub.model.Statistic;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findFirstByOrderByCreatedAtDesc();
    List<Statistic> findAllByOrderByCreatedAtDesc();

}
