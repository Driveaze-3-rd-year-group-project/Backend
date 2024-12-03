package com.driveaze.driveaze.service.interfac;

import com.driveaze.driveaze.dto.ResponseDTO;

public interface DashboardService {
    ResponseDTO getSupervisorStatistics();

    ResponseDTO getManagerStatistics();

    ResponseDTO getReceptionistStatistics();

    ResponseDTO getCustomerStatistics(int userId, String contack);

}
