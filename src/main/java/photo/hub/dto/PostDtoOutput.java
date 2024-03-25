package photo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoOutput {

    private long id;

    private String username;


    private String title;

    private String description;

    private int views;

    private String photoUrl;

    private LocalDate createdAt;
}
