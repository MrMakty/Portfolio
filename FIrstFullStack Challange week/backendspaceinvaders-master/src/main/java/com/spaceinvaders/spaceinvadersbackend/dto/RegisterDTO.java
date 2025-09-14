package com.spaceinvaders.spaceinvadersbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterDTO {

    @NotBlank(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")
    public String password;
    @NotBlank(message = "First name is required")
    public String firstName;
    @NotBlank(message = "Last name is required")
    public String lastName;
    @NotBlank(message = "Occupation is required")
    public String occupation;



    public RegisterDTO(String email, String password, String firstName, String lastName, String occupation) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }
}
