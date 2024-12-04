package com.driveaze.driveaze.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vehicle_brand")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBrand {
    @Id
    @Column(name = "brand_id", length =45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long brandId;

    @Column(name = "brand_name", length = 100, nullable = false)
    private String brandName;

    @Column(name = "registered_date", length =100, nullable = false)
    private LocalDate registeredDate;

    @Column(name = "brand_image")
    private String brandImage;
}
