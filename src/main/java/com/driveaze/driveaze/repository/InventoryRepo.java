package com.driveaze.driveaze.repository;

import com.driveaze.driveaze.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
    boolean existsByName(String name);
}
