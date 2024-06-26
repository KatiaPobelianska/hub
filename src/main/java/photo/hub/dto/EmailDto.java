package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EmailDto {
    @Email(message = "email should be valid")
    @NotBlank(message = "email can not be empty")
    private String email;
    @NotBlank(message = "subject can not be empty")
    private String subject;
    @NotBlank(message = "text can not be empty")
    private String text;

}
