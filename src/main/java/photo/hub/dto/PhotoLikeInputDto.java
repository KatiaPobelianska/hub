package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoLikeInputDto { // TODO без валидации?
   private long postId;

   private String username;
}
