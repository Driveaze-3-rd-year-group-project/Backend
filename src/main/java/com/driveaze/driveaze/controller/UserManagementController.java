package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.*;
import com.driveaze.driveaze.entity.CustomerVehicle;
import com.driveaze.driveaze.entity.OurUsers;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.service.AwsS3Service;
import com.driveaze.driveaze.service.impl.UserManagementService;
import com.driveaze.driveaze.service.interfac.IUserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
public class UserManagementController {
    @Autowired
    private IUserManagementService userService;
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private AwsS3Service awsS3Service;

    @GetMapping("/superuser/get-all-employees")
    public ResponseEntity<ResponseDTO> getAllEmployees() {
        return ResponseEntity.ok(userService.getAllEmployees());
    }

    //without managers and admins
    @GetMapping("/superuser/get-all-staff")
    public ResponseEntity<ResponseDTO> getAllStaff() {
        return ResponseEntity.ok(userService.getAllStaff());
    }

    @GetMapping("/superuser/get-all-customers")
    public ResponseEntity<ResponseDTO> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/superuser/get-user/{userId}")
    public ResponseEntity<ResponseDTO> getUsersById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUsersById(userId));
    }

    @GetMapping("/anyuser/get-profile")
    public ResponseEntity<ResponseDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ResponseDTO response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/superuser/delete/{userId}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @DeleteMapping("/customer/delete-account/{userId}")
    public ResponseEntity<ResponseDTO> deleteCustomer(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deleteCustomer(userId));
    }

//    @PutMapping("/customer/update-account/{userId}")
//    public ResponseEntity<ResponseDTO> updateCustomer(@PathVariable Integer userId, @RequestBody OurUserDTO ourUserDTO) {
//        return ResponseEntity.ok(userService.updateCustomer(userId, ourUserDTO));
//    }

    // Add the validation method
    private void validateFile(MultipartFile file) {
        if (file != null) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new OurException("Only image files are allowed");
            }

            // Check file size (e.g., max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new OurException("File size should not exceed 5MB");
            }
        }
    }


    //    @PutMapping("/employees/update/{userId}")
//    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody OurUsers request) {
//        return ResponseEntity.ok(userService.updateUser(userId, request));
//    }
    @PutMapping("/employees/update/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(
            @PathVariable Integer userId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userData") String userDataJson) {

        try {
            // Convert JSON string to OurUserDTO
            ObjectMapper mapper = new ObjectMapper();
            OurUserDTO ourUserDTO = mapper.readValue(userDataJson, OurUserDTO.class);

            // Upload image to S3 if file is present
            if (file != null && !file.isEmpty()) {
                String imageUrl = awsS3Service.saveImageToS3(file);
                ourUserDTO.setProfilePictureUrl(imageUrl);
            }

            // Update user data
            ResponseDTO response = userService.updateUser(userId, ourUserDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new OurException("Error updating employee: " + e.getMessage());
        }
    }

    @PutMapping("/customer/update-account/{userId}")
    public ResponseEntity<ResponseDTO> updateCustomer(
            @PathVariable Integer userId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userData") String userDataJson) {

        try {
            validateFile(file);

            // Convert JSON string to OurUserDTO
            ObjectMapper mapper = new ObjectMapper();
            OurUserDTO ourUserDTO = mapper.readValue(userDataJson, OurUserDTO.class);

            // Upload image to S3 if file is present
            if (file != null && !file.isEmpty()) {
                String imageUrl = awsS3Service.saveImageToS3(file);
                ourUserDTO.setProfilePictureUrl(imageUrl); // Assuming you have this field in OurUserDTO
            }

            // Update user data
            ResponseDTO response = userService.updateCustomer(userId, ourUserDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new OurException("Error updating customer: " + e.getMessage());
        }
    }



    @PutMapping("/anyuser/update-password/{userId}")
    public ResponseEntity<ResponseDTO> updatePassword(@PathVariable Integer userId, @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        return ResponseEntity.ok(userService.updatePassword(userId, passwordUpdateDTO));
    }

    @GetMapping("/superuser/search-supervisors")
    public List<OurUsers> serachSupervisors(@RequestParam("query") String query) {
        return userManagementService.searchBySupervisorName(query);
    }

}
