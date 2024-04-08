package photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import photo.hub.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getPostsByOwnerUsername(String username);
    List<Post> findByTitleContaining(String key);
    @Query("SELECT SUM(p.views) FROM Post p")
    Long getTotalViews();

    List<Post> findTop5ByOrderByViewsDesc();
}
