package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import photo.hub.model.Category;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostDtoOutput {

    private long id;

    private String username;

    private String title;

    private String description;

    private int views;

    private String photoUrl;

    private LocalDateTime createdAt;

    private Category category;

}
