package ma.projet.dents.repositories;

import ma.projet.dents.entities.Groupe;
import ma.projet.dents.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe,Integer> {
    List<Groupe> findByProfessor(Professor professor);

}
