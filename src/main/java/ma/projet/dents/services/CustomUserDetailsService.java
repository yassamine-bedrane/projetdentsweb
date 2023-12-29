package ma.projet.dents.services;

import ma.projet.dents.entities.MyUserDetails;
import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }

        MyUserDetails userDetails = new MyUserDetails(
                user.getUserName(),
                user.getPassword(),
                user.getLastName(),
                user.getRoles(),
                user.getFirstName());

        System.out.println("UserDetails: " + userDetails);

        return userDetails;
    }

}
