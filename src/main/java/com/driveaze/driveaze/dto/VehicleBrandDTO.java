package com.driveaze.driveaze.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBrandDTO {
    private Long brandId;
    private String brandName;
    private LocalDate registeredDate;
    private String brandImage;
}
