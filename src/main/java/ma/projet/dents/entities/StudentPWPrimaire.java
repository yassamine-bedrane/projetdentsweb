package ma.projet.dents.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentPWPrimaire implements Serializable {

    @ManyToOne
    @JoinColumn(name = "student_id",insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "pw_id",insertable = false, updatable = false)
    private PW pw;


    @Override
    public String toString() {
        return "StudentPWPrimaire{" +
                "student=" + student.getId() +
                ", pw=" + pw.getId() +
                '}';
    }

}
