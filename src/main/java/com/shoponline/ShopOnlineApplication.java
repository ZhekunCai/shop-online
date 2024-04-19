package com.shoponline;

import com.shoponline.entity.User;
import com.shoponline.repository.UserRepo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
@MapperScan(basePackages = {"com.shoponline.repository"})
public class ShopOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopOnlineApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepo userRepo, PasswordEncoder encoder) {
        return args -> userRepo.insert(new User(
                1L,
                "Admin",
                encoder.encode("123456"),
                User.Role.ADMIN,
                new Date()));
    }

}
