package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private long id;

    @NotBlank(message = "username can not be empty")
    @Size(min = 3, max = 15, message = "username length must be from 3 to 15 letters")
    private String username;

    @NotBlank(message = "email should be present")
    @Email(message = "email should be valid")
    private String email;

    @Size(min = 8, message = "minimal password length 8 signs")
    private String password;

    private String role;

}
