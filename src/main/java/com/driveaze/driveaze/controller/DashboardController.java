package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.service.interfac.DashboardService;
import com.driveaze.driveaze.service.interfac.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping(path = "/sup-statistic")
    public ResponseEntity<ResponseDTO> getStatistic(){
        return ResponseEntity.ok(dashboardService.getSupervisorStatistics());
    }

    @GetMapping(path = "/man-statistic")
    public ResponseEntity<ResponseDTO> getManagerStatistic(){
        return ResponseEntity.ok(dashboardService.getManagerStatistics());
    }

    @GetMapping(path = "/recp-statistic")
    public ResponseEntity<ResponseDTO> getReceptionistStatistic(){
        return ResponseEntity.ok(dashboardService.getReceptionistStatistics());
    }

    @GetMapping(path = "/cus-statistic/{userId}/{contact}")
    public ResponseEntity<ResponseDTO> getCustomerStatistic(@PathVariable Integer userId,@PathVariable String contact){
        return ResponseEntity.ok(dashboardService.getCustomerStatistics(userId,contact));
    }
}
