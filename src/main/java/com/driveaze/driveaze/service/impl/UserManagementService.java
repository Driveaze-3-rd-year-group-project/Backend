package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.OurUserDTO;
import com.driveaze.driveaze.dto.PasswordUpdateDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.auth.LoginRequest;
import com.driveaze.driveaze.entity.OurUsers;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.repository.UsersRepo;
import com.driveaze.driveaze.service.interfac.IUserManagementService;
import com.driveaze.driveaze.service.interfac.NotifyService;
import com.driveaze.driveaze.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService implements IUserManagementService {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotifyService notifyService;

    @Override
    public ResponseDTO employeeRegister(OurUsers employeeRegistrationRequest) {
        ResponseDTO resp = new ResponseDTO();

        try {
            if (employeeRegistrationRequest.getPassword() == null || employeeRegistrationRequest.getPassword().isEmpty()) {
                resp.setStatusCode(400);
                resp.setMessage("Password cannot be null or empty");
//                throw new OurException("Password cannot be null or empty");
                return resp;
            }

            Optional<OurUsers> existingUserByName = usersRepo.findByName(employeeRegistrationRequest.getName());
            if (existingUserByName.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Username is taken!");
//                throw new OurException("Username is taken!");
                return resp;
            }

            Optional<OurUsers> existingUserByEmail = usersRepo.findByEmail(employeeRegistrationRequest.getEmail());
            if (existingUserByEmail.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email is already registered!");
//                throw new OurException("Email is already registered!");
                return resp;
            }

            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(employeeRegistrationRequest.getEmail());
            ourUser.setRole(employeeRegistrationRequest.getRole());
            ourUser.setName(employeeRegistrationRequest.getName());
            ourUser.setRegisteredDate(LocalDate.now());
            ourUser.setContactNumber(employeeRegistrationRequest.getContactNumber());
            ourUser.setPassword(passwordEncoder.encode(employeeRegistrationRequest.getPassword()));

            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId() > 0) {
                resp.setOurUsers(ourUsersResult);
                resp.setMessage("User registered successfully");
                resp.setStatusCode(200);
            }


        }catch (OurException e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseDTO customerRegister(OurUsers customerRegistrationRequest) {
        ResponseDTO resp = new ResponseDTO();

        try {
            Optional<OurUsers> existingUserByEmail = usersRepo.findByEmail(customerRegistrationRequest.getEmail());
            if (existingUserByEmail.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email is already registered!");
                return resp;
            }

            Optional<OurUsers> existingUserByContactNumber = usersRepo.findByContactNumber(customerRegistrationRequest.getContactNumber());
            if (existingUserByContactNumber.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Contact number is already registered!");
                return resp;
            }

            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(customerRegistrationRequest.getEmail());
            ourUser.setRole("CUSTOMER");
            ourUser.setName(customerRegistrationRequest.getName());
            ourUser.setContactNumber(customerRegistrationRequest.getContactNumber());
            ourUser.setRegisteredDate(LocalDate.now());
            ourUser.setPassword(passwordEncoder.encode(customerRegistrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User registered successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseDTO login(LoginRequest loginRequest) {
        ResponseDTO response = new ResponseDTO();

        try {
            Optional<OurUsers> existingUserByEmail = usersRepo.findByEmail(loginRequest.getEmail());
            if (!existingUserByEmail.isPresent()) {
                response.setStatusCode(400);
                response.setMessage("Email not exists!");
                return response;
            }

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
            } catch (BadCredentialsException e) {
                response.setStatusCode(400);
                response.setMessage("Incorrect password!");
                return response;
            }

            var user = usersRepo.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("user Not found"));

            // Check if user is CUSTOMER and phone is not verified
            if ("CUSTOMER".equals(user.getRole()) && !user.isVerifiedPhone()) {
                try {
                    // Send OTP
                    notifyService.sendOtp(user.getContactNumber());

                    var jwt = jwtUtils.generateToken(user);

                    response.setStatusCode(202); // 202 Accepted - needs further action
                    response.setToken(jwt);
                    response.setRole(user.getRole());
                    response.setMessage("OTP sent to your phone number");
                    response.setUserId(user.getId());
                    response.setRequiresOTP(true); // Add this field to ResponseDTO
                    response.setPhoneNumber(user.getContactNumber());

                    return response;
                } catch (Exception e) {
                    response.setStatusCode(500);
                    response.setMessage("Failed to send OTP: " + e.getMessage());
                    return response;
                }
            }

            // Normal login flow for verified users or non-CUSTOMER users
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successfully logged in");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Add new method for OTP verification
    public ResponseDTO verifyOTP(String phoneNumber, String otp, Long userId) {
        ResponseDTO response = new ResponseDTO();

        try {
            if (otp == null || otp.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("OTP cannot be empty");
                return response;
            }

            // Verify OTP
            if (notifyService.verifyOtp(phoneNumber, otp)) {
                // Get user and update verification status
                OurUsers user = usersRepo.findById(Math.toIntExact(userId))
                        .orElseThrow(() -> new OurException("User not found"));

                user.setVerifiedPhone(true);
                usersRepo.save(user);

                // Generate JWT token and complete login
                var jwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRole(user.getRole());
                response.setExpirationTime("7 Days");
                response.setMessage("Phone verified and logged in successfully");
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid OTP");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAllEmployees() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<OurUsers> result = usersRepo.findByRoleOrRoleOrRoleOrRoleOrRoleOrRole( "ADMIN", "SUPERVISOR", "RECEPTIONIST", "MANAGER", "WAREHOUSE_KEEPER", "TECHNICIAN");
            if (!result.isEmpty()){
                response.setOurUsersList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("No Users Found");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured: " + e.getMessage());
            return response;
        }
    }

    @Override
    public ResponseDTO getAllStaff() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<OurUsers> result = usersRepo.findByRoleOrRoleOrRoleOrRole( "SUPERVISOR", "RECEPTIONIST", "WAREHOUSE_KEEPER", "TECHNICIAN");
            if (!result.isEmpty()){
                response.setOurUsersList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("No Staff Found");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured: " + e.getMessage());
            return response;
        }
    }

    @Override
    public ResponseDTO getAllCustomers() {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            List<OurUsers> result = usersRepo.findByRole( "CUSTOMER");
            if (!result.isEmpty()){
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No Users Found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occured: " + e.getMessage());
            return reqRes;
        }
    }

    @Override
    public ResponseDTO getUsersById(int userId) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            OurUsers usersById = usersRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + userId + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occured: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ResponseDTO deleteUser(int userId) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ResponseDTO updateUser(int userId, OurUserDTO updatedUser) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setRole(updatedUser.getRole());
                existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ResponseDTO getMyInfo(String email) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User Not Found");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public List<OurUsers> searchBySupervisorName(String query) {
        return usersRepo.findByRoleAndNameContainingIgnoreCase("SUPERVISOR", query);
    }

    @Override
    public ResponseDTO deleteCustomer(Integer userId) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);

            if (userOptional.isPresent()) {
                OurUsers user = userOptional.get();
                if ("CUSTOMER".equals(user.getRole())) { // Use equals() for string comparison
                    usersRepo.deleteById(userId);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Customer Account deleted successfully");
                } else {
                    reqRes.setStatusCode(400); // Bad Request instead of 404 (User not found is better suited for 404)
                    reqRes.setMessage("User is not a customer, cannot delete");
                }
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ResponseDTO updateCustomer(Integer userId, OurUserDTO ourUserDTO) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            // Fetch the user by ID
            Optional<OurUsers> userOptional = usersRepo.findById(userId);

            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();

                // Check if the role is CUSTOMER
                if ("CUSTOMER".equals(existingUser.getRole())) { // Use equals() for string comparison

                    // Check for duplicate contact number, excluding the current user
                    Optional<OurUsers> existingCustomerByContactNumber = usersRepo.findByContactNumber(ourUserDTO.getContactNumber());
                    if (existingCustomerByContactNumber.isPresent() &&
                            !existingCustomerByContactNumber.get().getId().equals(existingUser.getId())) {

                        reqRes.setStatusCode(400);
                        reqRes.setMessage("Contact Number Already Exists!");
                        return reqRes;
                    }

                    // Update the user's details
                    existingUser.setEmail(ourUserDTO.getEmail());
                    existingUser.setName(ourUserDTO.getName());
                    existingUser.setContactNumber(ourUserDTO.getContactNumber());
                    existingUser.setRole(ourUserDTO.getRole());
                    existingUser.setProfilePictureUrl(ourUserDTO.getProfilePictureUrl());

                    // Check if password is provided in the request
                    if (ourUserDTO.getPassword() != null && !ourUserDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(ourUserDTO.getPassword()));
                    }

                    // Save the updated user
                    OurUsers savedUser = usersRepo.save(existingUser);
                    reqRes.setOurUsers(savedUser);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Customer updated successfully");
                } else {
                    reqRes.setStatusCode(400);
                    reqRes.setMessage("User is not a customer, cannot update");
                }
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }

        return reqRes;
    }

    @Override
    public ResponseDTO updatePassword(Integer userId, PasswordUpdateDTO passwordUpdateDTO) {
        ResponseDTO reqRes = new ResponseDTO();

        try {
            // Fetch the user by ID
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();

                // Verify the current password
                if (passwordUpdateDTO.getCurrentPassword() == null ||
                        !passwordEncoder.matches(passwordUpdateDTO.getCurrentPassword(), existingUser.getPassword())) {
                    reqRes.setStatusCode(400);
                    reqRes.setMessage("Current password is incorrect");
                    return reqRes;
                }

                // Check if the new password is valid
                if (passwordUpdateDTO.getNewPassword() == null || passwordUpdateDTO.getNewPassword().isEmpty()) {
                    reqRes.setStatusCode(400);
                    reqRes.setMessage("New password cannot be empty");
                    return reqRes;
                }

                // Update the password
                existingUser.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
                usersRepo.save(existingUser);

                reqRes.setStatusCode(200);
                reqRes.setMessage("Password updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating password: " + e.getMessage());
        }

        return reqRes;
    }



}


