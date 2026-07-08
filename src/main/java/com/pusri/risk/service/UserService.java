package com.pusri.risk.service;

import com.pusri.risk.model.User;
import com.pusri.risk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User authenticate(String loginId, String password) {
        User user = userRepository.findByBadgeId(loginId);
        if (user == null) {
            user = userRepository.findFirstByNama(loginId);
        }
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public void registerAccount(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public void updateUser(User user) {
        userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAccount(Long id) {
        userRepository.deleteById(id);
    }
}
