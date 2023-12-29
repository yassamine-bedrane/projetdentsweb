package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.PW;
import ma.projet.dents.repositories.PWRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PWService implements IDao<PW> {
    @Autowired
    private PWRepository pwRepository;

    @Override
    public PW create(PW e) {
        return pwRepository.save(e);
    }

    @Override
    public boolean delete(PW e) {
        try {
            pwRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public PW update(PW e) {
        return pwRepository.save(e);
    }

    @Override
    public List<PW> findAll() {
        return pwRepository.findAll();
    }

    public PW findById(int id) {
        return pwRepository.findById(id).orElse(null);
    }

}
