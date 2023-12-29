package ma.projet.dents.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long alpha1;
    private Long alpha2;
    private Long alpha3;
    private Long beta1;
    private Long beta2;
    private Long beta3;


    @Lob
    @Column(name = "imageFront", columnDefinition="LONGBLOB")
    private byte[] imageFront;

    @ManyToOne
    private StudentPW studentpw;

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", imageFront=" + Arrays.toString(imageFront) +
                '}';
    }

    public Images(Long alpha1, Long alpha2, Long alpha3, Long beta1, Long beta2, Long beta3, byte[] imageFront, StudentPW studentpw) {
        this.alpha1 = alpha1;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.beta3 = beta3;
        this.imageFront = imageFront;
        this.studentpw = studentpw;
    }
}
