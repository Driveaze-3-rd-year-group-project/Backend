package com.driveaze.driveaze.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Email is required!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
}
