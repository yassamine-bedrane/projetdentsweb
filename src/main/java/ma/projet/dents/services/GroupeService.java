package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Groupe;
import ma.projet.dents.repositories.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeService implements IDao<Groupe> {
    @Autowired
    private GroupeRepository groupeRepository;

    @Override
    public Groupe create(Groupe e) {
        return groupeRepository.save(e);
    }

    @Override
    public boolean delete(Groupe e) {
        try {
            groupeRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Groupe update(Groupe e) {
        return groupeRepository.save(e);
    }

    @Override
    public List<Groupe> findAll() {
        return groupeRepository.findAll();
    }

    public Groupe findById(int id) {
        return groupeRepository.findById(id).orElse(null);
    }

}
