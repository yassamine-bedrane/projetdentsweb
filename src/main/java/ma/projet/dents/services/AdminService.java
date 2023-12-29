package ma.projet.dents.services;

import ma.projet.dents.dao.IDao;
import ma.projet.dents.entities.Admin;
import ma.projet.dents.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IDao<Admin> {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin create(Admin e) {
        return adminRepository.save(e);
    }

    @Override
    public boolean delete(Admin e) {
        try {
            adminRepository.delete(e);
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Admin update(Admin e) {
        return adminRepository.save(e);
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }


    public Admin findById(int id) {
        return adminRepository.findById(id).orElse(null);
    }

}
