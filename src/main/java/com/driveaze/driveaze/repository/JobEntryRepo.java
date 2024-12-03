package com.driveaze.driveaze.repository;

import com.driveaze.driveaze.entity.JobEntry;
import java.util.List;
import com.driveaze.driveaze.entity.JobRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface JobEntryRepo extends JpaRepository<JobEntry, Integer> {
    List<JobEntry> findByJobRegistry_JobId(int jobId);

    @Query("SELECT je, ou.name,ou.contactNumber FROM JobEntry je " +
            "JOIN OurUsers ou ON je.technicianId = ou.Id "+
            "WHERE je.jobRegistry.jobId=:jobId")
    List<Object[]> findEntryWithDetails(int jobId);

    @Query("SELECT je, ou.name,vb.brandName,vm.modelName,cv.vehicleNo FROM JobEntry je " +
            "JOIN OurUsers ou ON je.technicianId = ou.Id "+
            "JOIN CustomerVehicle cv ON je.jobRegistry.vehicleId = cv.vehicleId " +
            "JOIN VehicleModel vm ON cv.vehicleModelId = vm.modelId "+
            "JOIN VehicleBrand vb ON cv.vehicleBrandId = vb.brandId "+
            "WHERE je.technicianId=:userId")
    List<Object[]> findEntryWithTechnician(int userId);

    boolean existsByJobRegistry_JobId(Integer jobId);

    Page<JobEntry> findByJobRegistry_JobId(int jobId, Pageable pageable);

}
