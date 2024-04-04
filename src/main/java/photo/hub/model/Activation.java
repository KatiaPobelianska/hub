package photo.hub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "activation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "key")
    private String key;
    @Column(name = "username")
    private String username;

    public Activation(String key, String username) {
        this.key = key;
        this.username = username;
    }
}
