package com.pusri.risk;

import com.pusri.risk.model.User;
import com.pusri.risk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("ppip") == null) {
            User admin = new User();
            admin.setUsername("ppip");
            admin.setPassword("ppip");
            admin.setEmail("ppip@pusri.co.id");
            admin.setBadgeId("000000");
            admin.setNama("Super Admin");
            admin.setRole("Admin");
            admin.setDepartemen("IT / PPIP");
            userRepository.save(admin);
            System.out.println("Default admin user 'ppip' created.");
        }
    }
}
