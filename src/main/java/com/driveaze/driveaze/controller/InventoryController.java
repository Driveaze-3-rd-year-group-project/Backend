package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.CustomerVehicleDTO;
import com.driveaze.driveaze.dto.InventoryDTO;
import com.driveaze.driveaze.service.CustomerVehicleService;
import com.driveaze.driveaze.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping(path = "/add" )
    public String addItem(@RequestBody InventoryDTO inventoryDTO){
        String res = inventoryService.addInventoryItem(inventoryDTO);
        return res;
    }
    @PutMapping(path = "/update")
    public  String updateItem(@RequestBody InventoryDTO inventoryDTO) {
        String res = inventoryService.updateInventoryItem(inventoryDTO);
        return res;
    }

    @GetMapping(path = "/getAll")
    public List<InventoryDTO> getAllItems(){
        List<InventoryDTO> allItems = inventoryService.getAllItems();
        return allItems;
    }

    @DeleteMapping(path = "/delete/{id}")
    public String deleteItem(@PathVariable(value = "id") int itemId){
        String res = inventoryService.deleteInventoryItem(itemId);
        return res;
    }
}
