package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Professor;
import ma.projet.dents.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService implements IDao<Professor> {
    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public Professor create(Professor e) {
        return professorRepository.save(e);
    }

    @Override
    public boolean delete(Professor e) {
        try {
            professorRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Professor update(Professor e) {
        return professorRepository.save(e);
    }

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(int id) {
        return professorRepository.findById(id).orElse(null);
    }

}
