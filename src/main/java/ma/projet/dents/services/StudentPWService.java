package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.StudentPW;
import ma.projet.dents.entities.StudentPWPrimaire;
import ma.projet.dents.repositories.StudentPWRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentPWService implements IDao<StudentPW> {
    @Autowired
    private StudentPWRepository studentPWRepository;

    @Override
    public StudentPW create(StudentPW e) {
        return studentPWRepository.save(e);
    }

    @Override
    public boolean delete(StudentPW e) {
        try {
            studentPWRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public StudentPW update(StudentPW e) {
        return studentPWRepository.save(e);
    }

    @Override
    public List<StudentPW> findAll() {
        return studentPWRepository.findAll();
    }

    public StudentPW findById(StudentPWPrimaire id) {
        return studentPWRepository.findById(id).orElse(null);
    }

}
