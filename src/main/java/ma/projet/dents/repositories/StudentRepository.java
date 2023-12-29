package ma.projet.dents.repositories;

import ma.projet.dents.entities.Groupe;
import ma.projet.dents.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findByUserName(String userName);
    List<Student> findByGroupeIn(List<Groupe> groupes);
    @Query("SELECT s.userName, s.password FROM Student s")
    List<Object[]> findUsernamesAndPasswords();

}
