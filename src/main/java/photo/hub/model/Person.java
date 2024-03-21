package photo.hub.model;

import javax.persistence.*;
import javax.validation.constraints.Email;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username")
    @NotBlank(message = "username can not be empty")
    @Size(min = 3, max = 15, message = "username length must be from 3 to 15 letters")
    private String username;
    @Column(name = "email")
    @NotBlank(message = "email should be present")
    @Email(message = "email should be valid")
    private String email;
    @Column(name = "password")
    @Size(min = 8, message = "minimal password length 8 signs")
    private String password;
    @Column(name = "role")
    private String role;
}
