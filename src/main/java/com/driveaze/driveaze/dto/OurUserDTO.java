package com.driveaze.driveaze.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OurUserDTO {

    private Long Id;
    private String email;
    private String contactNumber;
    private String name;
    private String password;
    private String role;
}
