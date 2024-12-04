package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.VehicleBrandDTO;
import com.driveaze.driveaze.entity.JobRegistry;
import com.driveaze.driveaze.entity.VehicleBrand;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.repository.VehicleBrandRepo;
import com.driveaze.driveaze.service.interfac.VehicleBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleBrandServiceIMPL implements VehicleBrandService {

    @Autowired
    private VehicleBrandRepo vehicleBrandRepo;

    @Override
    public ResponseDTO addNewVehicleBrand(VehicleBrandDTO vehicleBrandDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            VehicleBrand vehicleBrand = new VehicleBrand(
                    vehicleBrandDTO.getBrandId(),
                    vehicleBrandDTO.getBrandName(),
                    vehicleBrandDTO.getRegisteredDate(),
                    vehicleBrandDTO.getBrandImage()
            );

            if(!vehicleBrandRepo.existsByBrandName(vehicleBrand.getBrandName())){
                vehicleBrandRepo.save(vehicleBrand);
                response.setStatusCode(200);
                response.setMessage("Successfully added vehicle brand");
            }else{
                response.setStatusCode(400);
                response.setMessage("Vehicle Brand already exists");
            }
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while adding vehicle Brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllVehicleBrands() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<VehicleBrand> vehicleBrands = vehicleBrandRepo.findAll();
            if (!vehicleBrands.isEmpty()){
                response.setVehicleBrandList(vehicleBrands);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No Vehicle Brands Found");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving vehicle Brands: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO updateVehicleBrand(Integer brandId, VehicleBrandDTO vehicleBrandDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            Optional<VehicleBrand> existingVehicleBrandByName = vehicleBrandRepo.findByBrandName(vehicleBrandDTO.getBrandName());
            if (existingVehicleBrandByName.isPresent()) {
                response.setStatusCode(400);
                response.setMessage("Vehicle Brand Already Exists!");
                return response;
            }

            VehicleBrand vehicleBrand = vehicleBrandRepo.findById(brandId)
                    .orElseThrow(() -> new OurException("Customer vehicle brand not found"));

            vehicleBrand.setBrandName(vehicleBrandDTO.getBrandName());
            vehicleBrand.setRegisteredDate(vehicleBrandDTO.getRegisteredDate());
            vehicleBrand.setBrandImage(vehicleBrandDTO.getBrandImage());

            vehicleBrandRepo.save(vehicleBrand);
            response.setStatusCode(200);
            response.setMessage("Successfully updated vehicle brand");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while updating vehicle Brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO deleteVehicleBrand(Integer brandId) {
        ResponseDTO response = new ResponseDTO();

        try {
            vehicleBrandRepo.findById(brandId).orElseThrow(()->new OurException("Vehicle Brand not found"));
            vehicleBrandRepo.deleteById(brandId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted vehicle brand");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting vehicle brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getVehicleBrandById(Integer brandId) {
        ResponseDTO response = new ResponseDTO();

        try {
            VehicleBrand vehicleBrand = vehicleBrandRepo.findById(brandId)
                    .orElseThrow(() -> new OurException("Vehicle Brand not found"));

            response.setVehicleBrand(vehicleBrand);
            response.setStatusCode(200);
            response.setMessage("Successfully Found Vehicle Brand");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving vehicle Brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Page<VehicleBrand> findBrandsWithPaginationAndSorting(int offset) {
        return vehicleBrandRepo.findAll(PageRequest.of(offset, 5).withSort(Sort.by(Sort.Order.desc("registeredDate"))));
    }

}
