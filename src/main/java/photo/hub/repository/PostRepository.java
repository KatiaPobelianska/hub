package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import photo.hub.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
