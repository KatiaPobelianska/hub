package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoInput {


    @NotBlank(message = "title can not be empty")
    @Size(min = 3, max = 25, message = "size must be from 3 to 25 letters")
    private String title;

    @NotBlank(message = "description can not be empty")
    @Size(min = 3, max = 150, message = "description must be from 3 to 150 letters")
    private String description;

    @NotNull
    private MultipartFile photo;
}
