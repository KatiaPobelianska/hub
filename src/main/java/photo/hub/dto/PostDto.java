package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import photo.hub.model.Person;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private long id;

    private Person owner;

    @NotBlank(message = "title can not be empty")
    @Size(min = 3, max = 25, message = "size must be from 3 to 25 letters")
    private String title;

    @NotBlank(message = "description can not be empty")
    @Size(min = 3, max = 150, message = "description must be from 3 to 150 letters")
    private String description;

    private int views;

    private byte[] photo;
    private LocalDate createdAt;

}
