package com.driveaze.driveaze.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OurUserDTO {

    private Long Id;
    private String email;
    private String contactNumber;
    private String name;
    private String password;
    private String role;
    private LocalDate registeredDate;
    private boolean isVerifiedPhone;
    private String profilePictureUrl;
}
