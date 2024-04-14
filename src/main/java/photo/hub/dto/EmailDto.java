package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @Email(message = "email should be valid") // TODO Обратите внимание, что null elements are considered valid.
    private String email;
    @NotBlank(message = "subject can not be empty")
    private String subject;
    @NotBlank(message = "text can not be empty")
    private String text;

}
