package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentOutputDto {

    private long id;
    private long postId;
    private String personUsername;
    private String description;
    private LocalDateTime createdAt;
}
