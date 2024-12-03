package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.service.interfac.DashboardService;
import com.driveaze.driveaze.service.interfac.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
