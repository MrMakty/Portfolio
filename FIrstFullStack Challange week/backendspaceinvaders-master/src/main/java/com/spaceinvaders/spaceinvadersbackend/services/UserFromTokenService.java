package com.spaceinvaders.spaceinvadersbackend.services;

import com.spaceinvaders.spaceinvadersbackend.dao.UserDAO;
import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserFromTokenService {
    private UserDAO userDAO;

    public UserFromTokenService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserEmail = (String) authentication.getPrincipal();

        return currentUserEmail;
    }

    public CustomUser getCurrentUser() {
        String email = getCurrentUserEmail();
        return this.userDAO.getByEmail(email);
    }



}
