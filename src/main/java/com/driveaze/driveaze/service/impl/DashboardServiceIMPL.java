package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.entity.*;
import com.driveaze.driveaze.repository.*;
import com.driveaze.driveaze.service.interfac.DashboardService;
import com.driveaze.driveaze.service.interfac.JobRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DashboardServiceIMPL implements DashboardService {

    @Autowired
    private JobRegistryRepo jobRegistryRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private CustomerVehicleRepo customerVehicleRepo;

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Override
    public ResponseDTO getSupervisorStatistics() {
        ResponseDTO response = new ResponseDTO();
        try {
            List<JobRegistry> allJobs = jobRegistryRepo.findAll();

            int totalJob = allJobs.size();
            int completed = 0;
            int pending = 0;
            int technicianCount = 0 ;

            for (JobRegistry job : allJobs) {
                if (job.getJobStatus() == 0) {
                    pending++;
                }

                if (job.getJobStatus() == 1) {
                    completed++;
                }
            }

            List<OurUsers> allUsers = usersRepo.findAll();

            for (OurUsers user : allUsers) {
                if (Objects.equals(user.getRole(), "TECHNICIAN")) {
                    technicianCount++;
                }
            }


            // Prepare response with statistics
            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("totalJob", totalJob);
            statistics.put("completedJobs", completed);
            statistics.put("pendingJobs", pending);
            statistics.put("technicianCount", technicianCount);

            response.setDetails(statistics);
            response.setStatusCode(200);
            response.setMessage("Supervisor statistics retrieved successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving supervisor statistics: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getManagerStatistics() {
        ResponseDTO response = new ResponseDTO();
        try {
            List<JobRegistry> allJobs = jobRegistryRepo.findAll();

            List<CustomerVehicle> allVehicles = customerVehicleRepo.findAll();

            int totalJob = allJobs.size();
            int totalVehicle = allVehicles.size();
            int completed = 0;
            int pending = 0;
            int technicianCount = 0 ;
            int supervisorCount = 0 ;
            int managerCount = 0 ;
            int whkeeperCount = 0 ;
            int receptionistCount = 0 ;
            int customerCount = 0;

            for (JobRegistry job : allJobs) {
                if (job.getJobStatus() == 0) {
                    pending++;
                }

                if (job.getJobStatus() == 1) {
                    completed++;
                }
            }

            List<OurUsers> allUsers = usersRepo.findAll();

            for (OurUsers user : allUsers) {
                if (Objects.equals(user.getRole(), "TECHNICIAN")) {
                    technicianCount++;
                }
                if (Objects.equals(user.getRole(), "SUPERVISOR")) {
                    supervisorCount++;
                }
                if (Objects.equals(user.getRole(), "MANAGER")) {
                    managerCount++;
                }
                if (Objects.equals(user.getRole(), "WAREHOUSE_KEEPER")) {
                    whkeeperCount++;
                }
                if (Objects.equals(user.getRole(), "MANAGER")) {
                    managerCount++;
                }
                if (Objects.equals(user.getRole(), "RECEPTIONIST")) {
                    receptionistCount++;
                }
                if (Objects.equals(user.getRole(), "CUSTOMER")) {
                    customerCount++;
                }

            }



            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("totalJob", totalJob);
            statistics.put("completedJobs", completed);
            statistics.put("pendingJobs", pending);
            statistics.put("supervisorCount", supervisorCount);
            statistics.put("technicianCount", technicianCount);
            statistics.put("totalVehicle", totalVehicle);
            statistics.put("managerCount", managerCount);
            statistics.put("whkeeperCount", whkeeperCount);
            statistics.put("receptionistCount", receptionistCount);
            statistics.put("customerCount", customerCount);

            response.setDetails(statistics);
            response.setStatusCode(200);
            response.setMessage("Supervisor statistics retrieved successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving supervisor statistics: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getReceptionistStatistics() {
        ResponseDTO response = new ResponseDTO();
        try {
            List<JobRegistry> allJobs = jobRegistryRepo.findAll();

            List<CustomerVehicle> allVehicles = customerVehicleRepo.findAll();

            List<Bill> allBill = billRepo.findAll();

            int totalJob = allJobs.size();
            int totalBill = allBill.size();
            int totalVehicle = allVehicles.size();
            int completed = 0;
            int pending = 0;
            int technicianCount = 0 ;
            int supervisorCount = 0 ;
            int managerCount = 0 ;
            int whkeeperCount = 0 ;
            int receptionistCount = 0 ;
            int customerCount = 0;

            for (JobRegistry job : allJobs) {
                if (job.getJobStatus() == 0) {
                    pending++;
                }

                if (job.getJobStatus() == 1) {
                    completed++;
                }
            }

            List<OurUsers> allUsers = usersRepo.findAll();

            for (OurUsers user : allUsers) {
                if (Objects.equals(user.getRole(), "TECHNICIAN")) {
                    technicianCount++;
                }
                if (Objects.equals(user.getRole(), "SUPERVISOR")) {
                    supervisorCount++;
                }
                if (Objects.equals(user.getRole(), "MANAGER")) {
                    managerCount++;
                }
                if (Objects.equals(user.getRole(), "WAREHOUSE_KEEPER")) {
                    whkeeperCount++;
                }
                if (Objects.equals(user.getRole(), "MANAGER")) {
                    managerCount++;
                }
                if (Objects.equals(user.getRole(), "RECEPTIONIST")) {
                    receptionistCount++;
                }
                if (Objects.equals(user.getRole(), "CUSTOMER")) {
                    customerCount++;
                }

            }



            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("totalJob", totalJob);
            statistics.put("completedJobs", completed);
            statistics.put("pendingJobs", pending);
            statistics.put("supervisorCount", supervisorCount);
            statistics.put("technicianCount", technicianCount);
            statistics.put("totalVehicle", totalVehicle);
            statistics.put("managerCount", managerCount);
            statistics.put("whkeeperCount", whkeeperCount);
            statistics.put("receptionistCount", receptionistCount);
            statistics.put("customerCount", customerCount);
            statistics.put("totalBill", totalBill);

            response.setDetails(statistics);
            response.setStatusCode(200);
            response.setMessage("Receptionist statistics retrieved successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving receptionist statistics: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getCustomerStatistics(int userId, String contact) {
        ResponseDTO response = new ResponseDTO();
        try {
            List<Booking> allbookings = bookingRepo.findAll();

            List<CustomerVehicle> allVehicles = customerVehicleRepo.findAll();

            int totalVehicle = allVehicles.size();
            int customerVehicle=0;
            int completed = 0;
            int pending = 0;

            for (Booking booking : allbookings) {
                if (booking.getCustomerId() == userId && booking.getStatus() == 0) {
                    pending++;
                }

                if (booking.getCustomerId() == userId && booking.getStatus() == 1) {
                    completed++;
                }
            }

            for (CustomerVehicle vehicle : allVehicles) {
                if (Objects.equals(vehicle.getOwnerPhone(), contact)) {
                    customerVehicle++;
                }
            }





            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("completedBooking", completed);
            statistics.put("pendingBooking", pending);
            statistics.put("customerVehicle", customerVehicle);
            statistics.put("totalVehicle", totalVehicle)


            response.setDetails(statistics);
            response.setStatusCode(200);
            response.setMessage("Customer statistics retrieved successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving customer statistics: " + e.getMessage());
        }

        return response;
    }

}
