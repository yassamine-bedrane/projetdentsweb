package ma.projet.dents.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User{
    private String number;

    @ManyToOne
    private Groupe groupe;

    @OneToMany(mappedBy = "pk.student")
    private List<StudentPW> studentpws;

    @Override
    public String toString() {
        return "Student{" +
                '}';
    }
}
