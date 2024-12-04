package com.driveaze.driveaze.repository;

import com.driveaze.driveaze.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface VehicleModelRepo extends JpaRepository<VehicleModel, Integer> {

    boolean existsByModelName(String modelName);

    Optional<VehicleModel> findByModelName(String modelName);

    List<VehicleModel> findByBrandId(Long brandId);

    @Query("SELECT vm FROM VehicleModel vm WHERE vm.modelName = :modelName AND vm.modelId != :currentId")
    Optional<VehicleModel> findByModelNameAndExcludeCurrent(@Param("modelName") String modelName, @Param("currentId") Integer currentId);

}
