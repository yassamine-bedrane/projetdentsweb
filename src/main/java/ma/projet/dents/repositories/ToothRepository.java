package ma.projet.dents.repositories;

import ma.projet.dents.entities.Tooth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToothRepository extends JpaRepository<Tooth,Integer> {
}
