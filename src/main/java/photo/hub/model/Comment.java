package photo.hub.model;

import javax.lang.model.element.Name;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "post_id")
    private long postId;
    @Column(name = "person_username")
    private String personUsername;
    @Column(name = "description")
    @NotBlank(message = "description can not be empty")
    @Size(min = 5, max = 150, message = "description can be from 5 to 150 letters")
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Comment(long postId, String personUsername, String description) {
        this.postId = postId;
        this.personUsername = personUsername;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}
