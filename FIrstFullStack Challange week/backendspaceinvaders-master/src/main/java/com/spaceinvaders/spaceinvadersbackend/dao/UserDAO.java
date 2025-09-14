package com.spaceinvaders.spaceinvadersbackend.dao;

import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CustomUser getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteByCustomUser(CustomUser user) {
        userRepository.delete(user);
    }
}
