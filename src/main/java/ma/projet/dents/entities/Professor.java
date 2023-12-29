package ma.projet.dents.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Professor extends User {
    private String grade;

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    private List<Groupe> groups;


    @Override
    public String toString() {
        return "Professor{" +
                "grade='" + grade + '\'' +
                '}';
    }
}
