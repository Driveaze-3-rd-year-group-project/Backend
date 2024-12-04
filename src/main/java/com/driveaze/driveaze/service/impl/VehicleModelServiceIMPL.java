package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.VehicleModelDTO;
import com.driveaze.driveaze.entity.CustomerVehicle;
import com.driveaze.driveaze.entity.VehicleBrand;
import com.driveaze.driveaze.entity.VehicleModel;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.repository.VehicleModelRepo;
import com.driveaze.driveaze.service.interfac.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleModelServiceIMPL implements VehicleModelService {

    @Autowired
    private VehicleModelRepo vehicleModelRepo;

    @Override
    public ResponseDTO addNewVehicleModel(VehicleModelDTO vehicleModelDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            VehicleModel vehicleModel = new VehicleModel(
                    vehicleModelDTO.getModelId(),
                    vehicleModelDTO.getBrandId(),
                    vehicleModelDTO.getModelName(),
                    vehicleModelDTO.getFuelType(),
                    vehicleModelDTO.getRegisteredDate()
            );

            if(!vehicleModelRepo.existsByModelName(vehicleModel.getModelName())){
                vehicleModelRepo.save(vehicleModel);
                response.setStatusCode(200);
                response.setMessage("Successfully added vehicle model");
            }else{
                response.setStatusCode(400);
                response.setMessage("vehicle model already exists");
            }
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while adding vehicle model: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllVehicleModels() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<VehicleModel> vehicleModels = vehicleModelRepo.findAll();
            if (!vehicleModels.isEmpty()){
                response.setVehicleModelList(vehicleModels);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No Vehicle models Found");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving vehicle models: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO updateVehicleModel(Integer modelId, VehicleModelDTO vehicleModelDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            // Check if a vehicle model with the same name exists, excluding the current model
            Optional<VehicleModel> existingVehicleModelByName =
                    vehicleModelRepo.findByModelNameAndExcludeCurrent(vehicleModelDTO.getModelName(), modelId);

            if (existingVehicleModelByName.isPresent()) {
                response.setStatusCode(400);
                response.setMessage("Vehicle Model Already Exists!");
                return response;
            }

            // Find the existing vehicle model by ID
            VehicleModel vehicleModel = vehicleModelRepo.findById(modelId)
                    .orElseThrow(() -> new OurException("Vehicle model not found"));

            // Update fields
            vehicleModel.setBrandId(vehicleModelDTO.getBrandId());
            vehicleModel.setModelName(vehicleModelDTO.getModelName());
            vehicleModel.setFuelType(vehicleModelDTO.getFuelType());
            vehicleModel.setRegisteredDate(vehicleModelDTO.getRegisteredDate());

            // Save changes
            vehicleModelRepo.save(vehicleModel);

            response.setStatusCode(200);
            response.setMessage("Successfully updated vehicle model");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating vehicle model: " + e.getMessage());
        }
        return response;
    }


    @Override
    public ResponseDTO deleteVehicleModel(Integer modelId) {
        ResponseDTO response = new ResponseDTO();

        try {
            vehicleModelRepo.findById(modelId).orElseThrow(()->new OurException("vehicle model not found"));
            vehicleModelRepo.deleteById(modelId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted vehicle model");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting vehicle model: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getVehicleModelById(Integer modelId) {
        ResponseDTO response = new ResponseDTO();

        try {
            VehicleModel vehicleModel = vehicleModelRepo.findById(modelId)
                    .orElseThrow(() -> new OurException("Vehicle Model not found"));

            response.setVehicleModel(vehicleModel);
            response.setStatusCode(200);
            response.setMessage("Successfully Found vehicle model");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving vehicle model: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllVehicleModelsWithBrandId(Long brandId) {
        ResponseDTO response = new ResponseDTO();

        try {
            // Fetch vehicle models associated with the given brand ID
            List<VehicleModel> vehicleModels = vehicleModelRepo.findByBrandId(brandId);

            if (!vehicleModels.isEmpty()) {
                response.setVehicleModelList(vehicleModels);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No Vehicle Models Found for the specified Brand ID");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving vehicle models: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Page<VehicleModel> findModelsWithPaginationAndSorting(int offset) {
        return vehicleModelRepo.findAll(PageRequest.of(offset, 5).withSort(Sort.by(Sort.Order.desc("registeredDate"))));
    }

}
