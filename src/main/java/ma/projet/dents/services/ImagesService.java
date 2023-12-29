package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Images;
import ma.projet.dents.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagesService implements IDao<Images> {
    @Autowired
    private ImagesRepository imagesRepository;

    @Override
    public Images create(Images e) {
        return imagesRepository.save(e);
    }

    @Override
    public boolean delete(Images e) {
        try {
            imagesRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Images update(Images e) {
        return imagesRepository.save(e);
    }

    @Override
    public List<Images> findAll() {
        return imagesRepository.findAll();
    }

    public Images findById(int id) {
        return imagesRepository.findById(id).orElse(null);
    }

}
