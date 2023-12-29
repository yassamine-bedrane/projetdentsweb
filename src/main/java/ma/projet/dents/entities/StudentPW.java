package ma.projet.dents.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPW {

    @EmbeddedId
    StudentPWPrimaire pk;

    @Temporal(TemporalType.DATE)
    private Date time;

    private Long note= 0L;;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(mappedBy = "studentpw")
    private List<Images> imagesList;

    @Override
    public String toString() {
        return "StudentPW{" +
                ", time='" + time + '\'' +
                ", date=" + date +
                '}';
    }

    public StudentPW(StudentPWPrimaire pk, Date time, Date date) {
        this.pk = pk;
        this.time = time;
        this.date = date;
    }
}
