package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import photo.hub.model.Activation;

import java.util.Optional;

@Repository
public interface ActivationRepository extends JpaRepository<Activation, Long> {
    Optional<Activation> findByKey(String key);

}
