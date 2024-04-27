package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentInputDto {

    private long postId;
    @NotBlank(message = "comment can not be blank")
    @Size(min = 1, max = 100, message = "minimal description length 1 sigh")
    private String description;

}
