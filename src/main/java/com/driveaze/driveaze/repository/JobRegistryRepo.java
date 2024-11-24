package com.driveaze.driveaze.repository;

import com.driveaze.driveaze.entity.CustomerVehicle;
import com.driveaze.driveaze.entity.JobRegistry;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface JobRegistryRepo extends JpaRepository<JobRegistry, Integer> {
    boolean existsByVehicleIdAndJobStatus(int vehicleId, int i);
}
