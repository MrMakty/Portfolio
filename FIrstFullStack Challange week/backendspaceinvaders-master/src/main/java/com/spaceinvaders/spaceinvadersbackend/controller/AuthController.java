package com.spaceinvaders.spaceinvadersbackend.controller;

import com.spaceinvaders.spaceinvadersbackend.config.JWTUtil;
import com.spaceinvaders.spaceinvadersbackend.dao.RoleRepository;
import com.spaceinvaders.spaceinvadersbackend.dao.UserRepository;
import com.spaceinvaders.spaceinvadersbackend.dto.LoginDTO;
import com.spaceinvaders.spaceinvadersbackend.dto.RegisterDTO;
import com.spaceinvaders.spaceinvadersbackend.dto.LoginResponse;
import com.spaceinvaders.spaceinvadersbackend.models.CustomUser;
import com.spaceinvaders.spaceinvadersbackend.models.ERole;
import com.spaceinvaders.spaceinvadersbackend.models.Role;
import com.spaceinvaders.spaceinvadersbackend.services.CredentialValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController

@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userDAO;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private CredentialValidator validator;
    private RoleRepository roleRepository;

    public AuthController(UserRepository userDAO, JWTUtil jwtUtil, AuthenticationManager authManager,
                          PasswordEncoder passwordEncoder, CredentialValidator validator, RoleRepository roleRepository) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (!validator.isValidEmail(registerDTO.email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid email provided"
            );
        }

        if (!validator.isValidPassword(registerDTO.password)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid password provided"
            );
        }

        CustomUser customUser = userDAO.findByEmail(registerDTO.email);

        if (customUser != null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not register with this email"
            );
        }
        String encodedPassword = passwordEncoder.encode(registerDTO.password);

        CustomUser registerdCustomUser = new CustomUser(registerDTO.email, encodedPassword, registerDTO.firstName, registerDTO.lastName, registerDTO.occupation);
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        registerdCustomUser.setRole(userRole.orElse(null));
        userDAO.save(registerdCustomUser);
        String token = jwtUtil.generateToken(registerdCustomUser.getEmail());
        LoginResponse loginResponse = new LoginResponse(registerdCustomUser.getEmail(),token);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.email, body.password);

            authManager.authenticate(authInputToken);
            CustomUser customUser = userDAO.findByEmail(body.email);
            String roleName = customUser.getRole().getName().toString();

            String token = jwtUtil.generateToken(body.email);


            LoginResponse loginResponse = new LoginResponse(customUser.getEmail(), token);


            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No valid credentials"
            );
        }
    }


}
