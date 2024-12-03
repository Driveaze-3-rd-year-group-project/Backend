package com.driveaze.driveaze.service.interfac;

import com.driveaze.driveaze.dto.OurUserDTO;
import com.driveaze.driveaze.dto.PasswordUpdateDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.dto.auth.LoginRequest;
import com.driveaze.driveaze.entity.OurUsers;

import java.util.List;

public interface IUserManagementService {

    ResponseDTO employeeRegister(OurUsers employeeRegistrationRequest);
    ResponseDTO customerRegister(OurUsers customerRegistrationRequest);
    ResponseDTO login(LoginRequest loginRequest);
    ResponseDTO getAllEmployees();
    ResponseDTO getAllStaff();
    ResponseDTO getAllCustomers();
    ResponseDTO getUsersById(int userId);
    ResponseDTO deleteUser(int userId);
    ResponseDTO updateUser(int userId, OurUserDTO updatedUser);
    ResponseDTO getMyInfo(String email);


    List<OurUsers> searchBySupervisorName(String query);

    ResponseDTO deleteCustomer(Integer userId);

    ResponseDTO updateCustomer(Integer userId, OurUserDTO ourUserDTO);

    ResponseDTO updatePassword(Integer userId, PasswordUpdateDTO passwordUpdateDTO);
}
