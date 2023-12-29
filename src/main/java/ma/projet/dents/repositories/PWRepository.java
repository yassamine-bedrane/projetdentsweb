
package ma.projet.dents.repositories;

import ma.projet.dents.entities.PW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PWRepository extends JpaRepository<PW,Integer> {
    List<PW> findByTitle(String title);

}
