package ma.projet.dents.repositories;

import ma.projet.dents.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Integer> {
    Professor findByUserName(String userName);

}
