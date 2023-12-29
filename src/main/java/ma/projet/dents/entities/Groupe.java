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
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String year;

    @ManyToOne
    private Professor professor;

    @OneToMany(mappedBy = "groupe")
    @JsonIgnore
    private List<Student> students;

    @ManyToMany
    @JsonIgnore
    private List<PW> pws;


    @Override
    public String toString() {
        return "Groupe{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

}
