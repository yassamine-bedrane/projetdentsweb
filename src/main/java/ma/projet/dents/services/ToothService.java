package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Tooth;
import ma.projet.dents.repositories.ToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToothService implements IDao<Tooth> {
    @Autowired
    private ToothRepository toothRepository;

    @Override
    public Tooth create(Tooth e) {
        return toothRepository.save(e);
    }

    @Override
    public boolean delete(Tooth e) {
        try {
            toothRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Tooth update(Tooth e) {
        return toothRepository.save(e);
    }

    @Override
    public List<Tooth> findAll() {
        return toothRepository.findAll();
    }

    public Tooth findById(int id) {
        return toothRepository.findById(id).orElse(null);
    }

}
