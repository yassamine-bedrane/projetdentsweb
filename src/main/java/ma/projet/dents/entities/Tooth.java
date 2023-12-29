package ma.projet.dents.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tooth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "tooth")
    @JsonIgnore
    private List<PW> pws;

    @Override
    public String toString() {
        return "Tooth{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
