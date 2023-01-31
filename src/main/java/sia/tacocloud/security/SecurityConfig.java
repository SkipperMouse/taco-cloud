package sia.tacocloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sia.tacocloud.model.User;
import sia.tacocloud.repository.UserRepository;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                return user;
            } else throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                    .requestMatchers("/design?continue", "/design", "/orders").hasRole("USER")
                    .requestMatchers("/", "/**").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/design")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                .build();
    }
}
