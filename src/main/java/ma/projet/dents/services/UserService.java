package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IDao<User> {
    @Autowired
    private UserRepository userRepository;



    @Override
    public User create(User e) {
        return userRepository.save(e);
    }

    @Override
    public boolean delete(User e) {
        try {
            userRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public User update(User e) {
        return userRepository.save(e);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }


}
