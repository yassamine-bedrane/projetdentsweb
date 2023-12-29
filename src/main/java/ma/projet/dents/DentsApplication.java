package ma.projet.dents;

import ma.projet.dents.entities.User;
import ma.projet.dents.repositories.RoleRepository;
import ma.projet.dents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import javax.management.relation.Role;

@SpringBootApplication
public class DentsApplication {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public static void main(String[] args) {
		SpringApplication.run(DentsApplication.class, args);
	}

	@Bean
    CommandLineRunner init() {
        return args -> {
            Role adminRole = Role.builder().name("ADMIN").build();
            Role profRole = Role.builder().name("PROF").build();

            roleRepository.save(adminRole);
            roleRepository.save(profRole);

            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .userName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Set.of(adminRole)) 
                    .build();

            User profUser = User.builder()
                    .firstName("Prof")
                    .lastName("User")
                    .userName("prof")
                    .password(passwordEncoder.encode("prof"))
                    .roles(Set.of(profRole)) 
                    .build();

            userRepository.save(adminUser);
            userRepository.save(profUser);
        };
    }
}
