package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.InventoryDTO;
import com.driveaze.driveaze.entity.Inventory;
import com.driveaze.driveaze.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import com.driveaze.driveaze.repository.InventoryRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvetoryServiceIMPL implements InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public String addInventoryItem(InventoryDTO inventoryDTO){
        Inventory inventory = new Inventory(
                inventoryDTO.getItemId(),
                inventoryDTO.getName(),
                inventoryDTO.getCount(),
                inventoryDTO.getInitialCount()
        );

        if(!inventoryRepo.existsByName(inventory.getName())){
            inventoryRepo.save(inventory);
            return "Added";
        }else{
            System.out.println("item already exists");
            return "item already exists";
        }
    }

    @Override
    public String updateInventoryItem(InventoryDTO inventoryDTO) {
        if(inventoryRepo.existsById(inventoryDTO.getItemId())){
            Inventory inventory=inventoryRepo.getById(inventoryDTO.getItemId());

            if((inventory.getName().equals(inventoryDTO.getName()) && inventoryRepo.existsByName(inventoryDTO.getName()))
            || (!inventory.getName().equals(inventoryDTO.getName()) && !inventoryRepo.existsByName(inventoryDTO.getName()))){
//                Inventory inventory=inventoryRepo.getById(inventoryDTO.getItemId());

                inventory.setName(inventoryDTO.getName());
                inventory.setCount(inventoryDTO.getCount());
                inventory.setInitialCount(inventoryDTO.getInitialCount());

                inventoryRepo.save(inventory);
                return "Updated";
            }else{
                System.out.println("item name already exists");
                return "item name already exists";
            }
        }else{
            System.out.println("No item found");
            return "No item found";
        }
    }

    @Override
    public List<InventoryDTO> getAllItems() {
        List<Inventory> items = inventoryRepo.findAll();
        List<InventoryDTO> inventoryDTOList = new ArrayList<>();

        for(Inventory inventory : items){
            InventoryDTO inventoryDTO = new InventoryDTO(
                    inventory.getItemId(),
                    inventory.getName(),
                    inventory.getCount(),
                    inventory.getInitialCount()
            );
            inventoryDTOList.add(inventoryDTO);
        }
        return inventoryDTOList;
    }

    @Override
    public String deleteInventoryItem(int itemId) {
        if(inventoryRepo.existsById(itemId)){
            inventoryRepo.deleteById(itemId);
            return "deleted";
        }else{
            System.out.println("No item found under this id");
            return "No item found under this id.";
        }
    }
}
