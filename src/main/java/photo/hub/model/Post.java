package photo.hub.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne()
    @JoinColumn(name = "owner_username", referencedColumnName = "username")
    private Person owner;
    @Column(name = "title")
    @NotBlank(message = "title can not be empty")
    @Size(min = 3, max = 25, message = "size must be from 3 to 25 letters")
    private String title;
    @Column(name = "description")
    @NotBlank(message = "description can not be empty")
    @Size(min = 3, max = 150, message = "description must be from 3 to 150 letters")
    private String description;
    @Column(name = "views")
    private int views;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
