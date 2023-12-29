package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Student;
import ma.projet.dents.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IDao<Student> {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student create(Student e) {
        return studentRepository.save(e);
    }

    @Override
    public boolean delete(Student e) {
        try {
            studentRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Student update(Student e) {
        return studentRepository.save(e);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

}
