package ma.projet.dents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(t -> t.disable());
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/app/users/role").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/mdprecuperation/**").permitAll()
                        
                        .requestMatchers("/app/admins/**").hasRole("ADMIN")
                        .requestMatchers("/app/professors/**").hasRole("ADMIN")

                        .requestMatchers("/app/studentpws/**").hasRole("PROF")
                        .requestMatchers("/profile/**").hasRole("PROF")
                        .requestMatchers("/app/students/**").hasRole("PROF")
                        .requestMatchers("/app/pws/**").hasRole("PROF")
                        .requestMatchers("/app/groupes/**").hasRole("PROF")
                        .requestMatchers("/app/tooths/**").hasRole("PROF")
                        .requestMatchers("/app/imagess/**").hasRole("PROF")
                        
                        .requestMatchers("/students/**").permitAll()

                        .anyRequest().authenticated()
        );
        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/app/users/role", true).permitAll());

        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login?logout").permitAll()
        );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
