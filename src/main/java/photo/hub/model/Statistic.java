package photo.hub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "statistic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "total_views")
    private long totalViews;
    @Column(name = "total_likes")
    private long totalLikes;
    @Column(name = "total_comments")
    private long totalComments;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Statistic(long totalViews, long totalLikes, long totalComments) {
        this.totalViews = totalViews;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.createdAt = LocalDateTime.now();
    }
}
