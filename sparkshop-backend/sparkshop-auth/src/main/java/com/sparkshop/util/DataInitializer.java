package com.sparkshop.util;

import com.sparkshop.model.User;
import com.sparkshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@sparkshop.com";
        if (!repo.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .name("Admin")
                    .email(adminEmail)
                    .password(encoder.encode("Admin@123")) // change before prod
                    .roles(List.of("ADMIN"))
                    .build();
            repo.save(admin);
            System.out.println("Created admin user: " + adminEmail + " / Admin@123");
        }
    }
}
