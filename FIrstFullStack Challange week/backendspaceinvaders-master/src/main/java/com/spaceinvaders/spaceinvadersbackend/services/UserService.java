package com.spaceinvaders.spaceinvadersbackend.services;

import com.spaceinvaders.spaceinvadersbackend.dao.UserRepository;
import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findByEmail(email);
        return new User(email, customUser.getPassword(), Collections.singleton(new SimpleGrantedAuthority(customUser.getRole().getName().toString())));
    }
}
