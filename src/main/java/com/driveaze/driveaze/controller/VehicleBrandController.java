package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.OurUserDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.VehicleBrandDTO;
import com.driveaze.driveaze.entity.JobRegistry;
import com.driveaze.driveaze.entity.VehicleBrand;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.service.AwsS3Service;
import com.driveaze.driveaze.service.interfac.VehicleBrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vehicle-brand")
@CrossOrigin
public class VehicleBrandController {
    @Autowired
    private VehicleBrandService vehicleBrandService;

    @Autowired
    private AwsS3Service awsS3Service;

    @PostMapping(path = "/save" )
    public ResponseEntity<ResponseDTO> addNewVehicleBrand(@RequestBody VehicleBrandDTO vehicleBrandDTO){
        return ResponseEntity.ok(vehicleBrandService.addNewVehicleBrand(vehicleBrandDTO));
    }
//    private void validateFile(MultipartFile file) {
//        if (file != null) {
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")) {
//                throw new OurException("Only image files are allowed");
//            }
//
//            // Check file size (e.g., max 5MB)
//            if (file.getSize() > 5 * 1024 * 1024) {
//                throw new OurException("File size should not exceed 5MB");
//            }
//        }
//    }

//    @PostMapping(path = "/save")
//    public ResponseEntity<ResponseDTO> addNewVehicleBrand(
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestParam("userData") String userDataJson) {
//
//        try {
//            validateFile(file);
//
//            // Convert JSON string to OurUserDTO
//            ObjectMapper mapper = new ObjectMapper();
//            VehicleBrandDTO vehicleBrandDTO = mapper.readValue(userDataJson, VehicleBrandDTO.class);
//
//            // Upload image to S3 if file is present
//            if (file != null && !file.isEmpty()) {
//                String imageUrl = awsS3Service.saveImageToS3(file);
//                vehicleBrandDTO.setBrandImage(imageUrl);
//            }
//
//            // Update user data
//            ResponseDTO response = vehicleBrandService.addNewVehicleBrand(vehicleBrandDTO);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            throw new OurException("Error updating employee: " + e.getMessage());
//        }
//    }

    @PutMapping(path = "/update/{brandId}")
    public ResponseEntity<ResponseDTO> updateVehicleBrand(@PathVariable Integer brandId, @RequestBody VehicleBrandDTO vehicleBrandDTO) {
        ResponseDTO responseDTO = vehicleBrandService.updateVehicleBrand(brandId, vehicleBrandDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<ResponseDTO> deleteVehicleBrand(@PathVariable Integer brandId) {
        return ResponseEntity.ok(vehicleBrandService.deleteVehicleBrand(brandId));
    }

    @GetMapping("/get-all-vehicle-brands")
    public ResponseEntity<ResponseDTO> getAllVehicleBrands() {
        return ResponseEntity.ok(vehicleBrandService.getAllVehicleBrands());
    }

    @GetMapping("/get-vehicle-brand/{brandId}")
    public ResponseEntity<ResponseDTO> getVehicleBrandById(@PathVariable Integer brandId) {
        return ResponseEntity.ok(vehicleBrandService.getVehicleBrandById(brandId));
    }

    @GetMapping("/paginationAndSort/{offset}")
    public Page<VehicleBrand> findBrandsWithPaginationAndSorting(@PathVariable int offset) {
        return vehicleBrandService.findBrandsWithPaginationAndSorting(offset);
    }
}
