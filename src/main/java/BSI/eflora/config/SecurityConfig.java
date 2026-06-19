package BSI.eflora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
             
                .requestMatchers(
                    "/", 
                    "/home",
                    "/login",
                    "/css/**", 
                    "/js/**", 
                    "/images/**"
                ).permitAll()

              
                .requestMatchers("/admin/**").authenticated()

                .anyRequest().permitAll()
            )

            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/")
            );

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService users() {
        UserDetails user = User
                .withUsername("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}