package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import photo.hub.model.PhotoLike;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long> {
    int countPhotoLikeByPostId(long postId);
    Optional<PhotoLike> findByPostIdAndPersonUsername(long postId, String username);
    void deleteByPostIdAndPersonUsername(long postId, String username);

}
