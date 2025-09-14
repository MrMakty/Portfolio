package com.spaceinvaders.spaceinvadersbackend.controller;

import com.spaceinvaders.spaceinvadersbackend.dao.UserDAO;
import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.services.UserFromTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {
    private UserDAO userDAO;
    private UserFromTokenService userFromTokenService;

    public UserController(UserDAO userDAO, UserFromTokenService userFromTokenService) {
        this.userDAO = userDAO;
        this.userFromTokenService = userFromTokenService;
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomUser> authenticatedUser() {
        CustomUser customUser = this.userDAO.getByEmail(userFromTokenService.getCurrentUserEmail());
        return ResponseEntity.ok(customUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteAuthenticatedUser() {
        CustomUser currentUser = userDAO.getByEmail(userFromTokenService.getCurrentUserEmail());
        userDAO.deleteByCustomUser(currentUser);

        return ResponseEntity.ok().body("Your account has been deleted.");
    }
}
