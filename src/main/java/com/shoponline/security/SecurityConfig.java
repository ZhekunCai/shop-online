package com.shoponline.security;

import com.shoponline.entity.User;
import com.shoponline.repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/order/**").hasRole("CUSTOMER")
                        .requestMatchers("/salesman/**").hasRole("SALESMAN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/*.ico", "/image/**", "/lib/**", "/upload/**",
                                "/", "/error", "/register", "/login", "/itemList").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                )
                .logout((logout) -> logout.permitAll())
                .rememberMe((rememberMe) -> rememberMe.key("NegativeHappyChainsawEdge"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return (username) -> {
            List<User> users = userRepo.find(null, username, null);
            if (users == null || users.isEmpty()) {
                throw new UsernameNotFoundException("Username Not Found");
            }
            return users.get(0);
        };
    }

}
