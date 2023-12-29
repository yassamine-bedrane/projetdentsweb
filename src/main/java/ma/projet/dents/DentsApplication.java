package ma.projet.dents;

import ma.projet.dents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class DentsApplication {
	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(DentsApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//			var user1 = User.builder()
//					.firstName("bouchra")
//					.lastName("bouchra")
//					.userName("bouchra")
//					// .password("$2a$10$gqHrslMttQWSsDSVRTK1OehkkBiXsJ/a4z2OURU./dizwOQu5Lovu")
//					.password(passwordEncoder.encode("bouchra"))
//					.build();
//
//			userRepository.save(user1);


		};
	}
}
