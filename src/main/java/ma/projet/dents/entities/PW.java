package ma.projet.dents.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;



@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PW {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String objectif;
    private String docs;



    @OneToMany(mappedBy = "pk.pw")
    @JsonIgnore
    private List<StudentPW> studentpws;

    @ManyToMany(mappedBy = "pws")
    @JsonIgnore
    private List<Groupe> groups;

    @ManyToOne
    private Tooth tooth;

    @Override
    public String toString() {
        return "PW{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", objectif='" + objectif + '\'' +
                ", docs='" + docs + '\'' +
                '}';
    }
}
