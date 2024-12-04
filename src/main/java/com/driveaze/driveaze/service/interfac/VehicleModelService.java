package com.driveaze.driveaze.service.interfac;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.VehicleModelDTO;
import com.driveaze.driveaze.entity.VehicleBrand;
import com.driveaze.driveaze.entity.VehicleModel;
import org.springframework.data.domain.Page;

public interface VehicleModelService {
    ResponseDTO addNewVehicleModel(VehicleModelDTO vehicleModelDTO);

    ResponseDTO getAllVehicleModels();

    ResponseDTO updateVehicleModel(Integer modelId, VehicleModelDTO vehicleModelDTO);

    ResponseDTO deleteVehicleModel(Integer modelId);

    ResponseDTO getVehicleModelById(Integer modelId);

    ResponseDTO getAllVehicleModelsWithBrandId(Long brandId);

    Page<VehicleModel> findModelsWithPaginationAndSorting(int offset);
}
