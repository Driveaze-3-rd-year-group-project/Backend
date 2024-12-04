package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.VehicleModelDTO;
import com.driveaze.driveaze.entity.VehicleBrand;
import com.driveaze.driveaze.entity.VehicleModel;
import com.driveaze.driveaze.service.interfac.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle-model")
@CrossOrigin
public class VehicleModelController {

    @Autowired
    private VehicleModelService vehicleModelService;

    @PostMapping(path = "/save" )
    public ResponseEntity<ResponseDTO> addNewVehicleModel(@RequestBody VehicleModelDTO vehicleModelDTO){
        return ResponseEntity.ok(vehicleModelService.addNewVehicleModel(vehicleModelDTO));
    }

    @PutMapping(path = "/update/{modelId}")
    public ResponseEntity<ResponseDTO> updateVehicleModel(@PathVariable Integer modelId, @RequestBody VehicleModelDTO vehicleModelDTO) {
        ResponseDTO responseDTO = vehicleModelService.updateVehicleModel(modelId, vehicleModelDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/delete/{modelId}")
    public ResponseEntity<ResponseDTO> deleteVehicleModel(@PathVariable Integer modelId) {
        return ResponseEntity.ok(vehicleModelService.deleteVehicleModel(modelId));
    }

    @GetMapping("/get-all-vehicle-models")
    public ResponseEntity<ResponseDTO> getAllVehicleModels() {
        return ResponseEntity.ok(vehicleModelService.getAllVehicleModels());
    }

    @GetMapping("/get-all-vehicle-models-with-brand-id/{brandId}")
    public ResponseEntity<ResponseDTO> getAllVehicleModelsWithBrandId(@PathVariable Long brandId) {
        return ResponseEntity.ok(vehicleModelService.getAllVehicleModelsWithBrandId(brandId));
    }

    @GetMapping("/get-vehicle-model/{modelId}")
    public ResponseEntity<ResponseDTO> getVehicleModelById(@PathVariable Integer modelId) {
        return ResponseEntity.ok(vehicleModelService.getVehicleModelById(modelId));
    }

    @GetMapping("/paginationAndSort/{offset}")
    public Page<VehicleModel> findModelsWithPaginationAndSorting(@PathVariable int offset) {
        return vehicleModelService.findModelsWithPaginationAndSorting(offset);
    }
}
