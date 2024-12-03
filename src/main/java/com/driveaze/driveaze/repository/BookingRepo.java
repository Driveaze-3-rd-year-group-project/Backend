package com.driveaze.driveaze.repository;
import com.driveaze.driveaze.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@EnableJpaRepositories
@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    boolean existsByCustomerIdAndVehicleNoAndStatus(Long customerId, String vehicleNo, int i);
    long countByPreferredDate(LocalDate preferredDate);
}