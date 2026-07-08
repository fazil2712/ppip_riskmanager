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
        if (userRepository.findByBadgeId("000000") == null) {
            User admin = new User();
            admin.setBadgeId("000000");
            admin.setPassword("ppip");
            admin.setNama("Super Admin");
            admin.setEmail("ppip@pusri.co.id");
            admin.setRole("Admin");
            admin.setDepartemen("IT / PPIP");
            userRepository.save(admin);
            System.out.println("Default admin user 'ppip' created with Badge ID 000000.");
        }
    }
}
