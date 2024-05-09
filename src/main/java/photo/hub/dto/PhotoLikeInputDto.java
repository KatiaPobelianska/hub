package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoLikeInputDto {
    @NotBlank(message = "post_id must be present")
    private long postId;
    @NotBlank(message = "username must be present")
    private String username;
}
