package com.spaceinvaders.spaceinvadersbackend.dto;


import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")
    public String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

