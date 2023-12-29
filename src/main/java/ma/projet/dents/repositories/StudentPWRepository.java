package ma.projet.dents.repositories;

import ma.projet.dents.entities.StudentPW;
import ma.projet.dents.entities.StudentPWPrimaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPWRepository extends JpaRepository<StudentPW, StudentPWPrimaire> {

}
