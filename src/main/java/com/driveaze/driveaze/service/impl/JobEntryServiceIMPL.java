package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.dto.JobEntryDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.entity.*;
import com.driveaze.driveaze.exception.OurException;
import com.driveaze.driveaze.repository.*;
import com.driveaze.driveaze.repository.BillEntryRepo;
import com.driveaze.driveaze.service.interfac.JobEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class JobEntryServiceIMPL implements JobEntryService {

    @Autowired
    private JobEntryRepo jobEntryRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JobRegistryRepo jobRegistryRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private BillEntryRepo billEntryRepo;

    @Autowired
    private JobEntryItemRepo jobEntryItemRepo;


//    @Override
//    public ResponseDTO addNewJobEntry(JobEntryDTO jobEntryDTO) {
//        ResponseDTO response = new ResponseDTO();
//
//        try {
//            // Fetch JobRegistry
//            JobRegistry jobRegistry = jobRegistryRepo.findById(jobEntryDTO.getJobRegistry().getJobId())
//                    .orElseThrow(() -> new OurException("Job Registry not found"));
//
//
//            if (jobEntryDTO.getInventoryItemList() != null) {
//                List<Map<String, String>> inventoryItemList;
//
//                // Handle potential type casting
//                if (jobEntryDTO.getInventoryItemList() instanceof List) {
//                    inventoryItemList = (List<Map<String, String>>) jobEntryDTO.getInventoryItemList();
//                } else {
//                    throw new OurException("Invalid inventory item list format");
//                }
//
//                // Reduce inventory for each item
//                for (Map<String, String> item : inventoryItemList) {
//                    String id = item.get("id");
//                    String quantity = item.get("quantity");
//
//                    if (id == null || quantity == null) {
//                        throw new OurException("Invalid inventory item details");
//                    }
//
//                    ResponseDTO inventoryResponse = reduceItem(
//                            Integer.parseInt(id),
//                            Integer.parseInt(quantity)
//                    );
//
//                    // Check if inventory reduction was successful
//                    if (inventoryResponse.getStatusCode() != 200) {
//                        throw new OurException("Failed to reduce inventory: " + inventoryResponse.getMessage());
//                    }
//                }
//            }
//
//            // Create JobEntry entity
//            JobEntry jobEntry = new JobEntry(
//                    0, // jobEntryId will be auto-generated
//                    jobRegistry,
//                    jobEntryDTO.getEntryDate(),
//                    jobEntryDTO.getTime(),
//                    jobEntryDTO.getTechnicianId(),
//                    jobEntryDTO.getDetails(),
//                    jobEntryDTO.getManHours()
//            );
//
//            jobEntryRepo.save(jobEntry);
//
//            response.setStatusCode(200);
//            response.setMessage("Successfully added job entry");
//        } catch (OurException e) {
//            response.setStatusCode(404);
//            response.setMessage(e.getMessage());
//        } catch (Exception e) {
//            response.setStatusCode(500);
//            response.setMessage("Error occurred while adding job entry: " + e.getMessage());
//        }
//
//        return response;
//    }
    @Override
    public ResponseDTO addNewJobEntry(JobEntryDTO jobEntryDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            // Fetch JobRegistry
            JobRegistry jobRegistry = jobRegistryRepo.findById(jobEntryDTO.getJobRegistry().getJobId())
                    .orElseThrow(() -> new OurException("Job Registry not found"));

            // Initialize a list to store all BillEntries
            List<BillEntry> billEntries = new ArrayList<>();
            List<JobEntryItem> jobEntryItems = new ArrayList<>();

            if (jobEntryDTO.getInventoryItemList() != null) {
                List<Map<String, String>> inventoryItemList;

                // Handle potential type casting
                if (jobEntryDTO.getInventoryItemList() instanceof List) {
                    inventoryItemList = (List<Map<String, String>>) jobEntryDTO.getInventoryItemList();
                } else {
                    throw new OurException("Invalid inventory item list format");
                }

                // Process each inventory item
                for (Map<String, String> item : inventoryItemList) {
                    String id = item.get("id");
                    String quantity = item.get("quantity");

                    if (id == null || quantity == null) {
                        throw new OurException("Invalid inventory item details");
                    }

                    // Fetch inventory item details
                    Inventory inventoryItem = inventoryRepo.findById(Integer.parseInt(id))
                            .orElseThrow(() -> new OurException("Inventory item not found for ID: " + id));

                    // Reduce inventory
                    ResponseDTO inventoryResponse = reduceItem(
                            Integer.parseInt(id),
                            Integer.parseInt(quantity)
                    );

                    // Check if inventory reduction was successful
                    if (inventoryResponse.getStatusCode() != 200) {
                        throw new OurException("Failed to reduce inventory: " + inventoryResponse.getMessage());
                    }

                    // Fetch the Bill entity
                    Bill bill = billRepo.findByJobRegistry_JobId(jobRegistry.getJobId())
                            .orElseThrow(() -> new OurException("Bill not found for the job registry"));

                    // Create a new BillEntry for this inventory item
                    BillEntry billEntry = new BillEntry(
                            0, // billEntryId will be auto-generated
                            bill,
                            jobEntryDTO.getEntryDate(),
                            jobEntryDTO.getTime(),
                            inventoryItem.getName(), // Service or product name
                            Integer.parseInt(quantity),
                            inventoryItem.getSellingPrice().multiply(new BigDecimal(quantity)), // Total price calculation
                            inventoryItem.getSellingPrice()
                    );

                    // Add the BillEntry to the list
                    billEntries.add(billEntry);

                    // Create a new JobEntryItem for this inventory item
                    JobEntryItem jobEntryItem = new JobEntryItem(
                            0, // jobEntryItemId will be auto-generated
                            null, // Placeholder for JobEntry, will be updated later
                            jobEntryDTO.getEntryDate(),
                            jobEntryDTO.getTime(),
                            inventoryItem.getName(),
                            Integer.parseInt(quantity)
                    );

                    // Add the JobEntryItem to the list
                    jobEntryItems.add(jobEntryItem);
                }
            }

            // Save all BillEntries in one batch operation
            billEntryRepo.saveAll(billEntries);

            // Create JobEntry entity
            JobEntry jobEntry = new JobEntry(
                    0, // jobEntryId will be auto-generated
                    jobRegistry,
                    jobEntryDTO.getEntryDate(),
                    jobEntryDTO.getTime(),
                    jobEntryDTO.getTechnicianId(),
                    jobEntryDTO.getDetails(),
                    jobEntryDTO.getManHours()
            );

            // Save JobEntry to generate its ID
            JobEntry savedJobEntry = jobEntryRepo.save(jobEntry);

            // Assign the saved JobEntry to all JobEntryItems
            for (JobEntryItem jobEntryItem : jobEntryItems) {
                jobEntryItem.setJobEntry(savedJobEntry);
            }

            // Save all JobEntryItems in one batch operation
            jobEntryItemRepo.saveAll(jobEntryItems);

            Bill bill = billRepo.findByJobRegistry_JobId(jobRegistry.getJobId())
                    .orElseThrow(() -> new OurException("Bill not found for the job registry"));


            BillEntry billEntry = new BillEntry(
                    0, // billEntryId will be auto-generated
                    bill,
                    jobEntryDTO.getEntryDate(),
                    jobEntryDTO.getTime(),
                    jobEntryDTO.getDetails() + " (Service Charge)", // Append "(Service)" to details
                    1, // Default quantity for service
                    jobEntryDTO.getManHours().multiply(new BigDecimal("50")), // Example calculation for total price
                    jobEntryDTO.getManHours().multiply(new BigDecimal("50"))
            );

            billEntryRepo.save(billEntry);

            response.setStatusCode(200);
            response.setMessage("Successfully added job entry and updated bill entry");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while adding job entry: " + e.getMessage());
        }

        return response;
    }



    @Override
    public ResponseDTO getAllEntriesOfJob(Integer jobId) {
        ResponseDTO response = new ResponseDTO();

        try {
            List<Object[]> jobEntryDetails = jobEntryRepo.findEntryWithDetails(jobId);
//            List<JobEntry> jobEntries = jobEntryRepo.findByJobRegistry_JobId(jobId);;
            if (!jobEntryDetails.isEmpty()){
                response.setDetails(jobEntryDetails);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No Job entries Found");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving job entries: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO updateJobEntry(Integer jobEntryId, JobEntryDTO jobEntryDTO) {
        ResponseDTO response = new ResponseDTO();

        try {
            JobEntry jobEntry = jobEntryRepo.findById(jobEntryId)
                    .orElseThrow(() -> new OurException("Job Entry not found"));

            jobEntry.setEntryDate(jobEntryDTO.getEntryDate());
            jobEntry.setTime(jobEntryDTO.getTime());
            jobEntry.setDetails(jobEntryDTO.getDetails());
            jobEntry.setManHours(jobEntryDTO.getManHours());
            jobEntry.setTechnicianId(jobEntryDTO.getTechnicianId());

            jobEntryRepo.save(jobEntry);
            response.setStatusCode(200);
            response.setMessage("Successfully updated job entry");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while updating job entry: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO deleteJobEntry(Integer jobEntryId) {
        ResponseDTO response = new ResponseDTO();

        try {
            jobEntryRepo.findById(jobEntryId).orElseThrow(()->new OurException("Job entry not found"));
            jobEntryRepo.deleteById(jobEntryId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted Job entry");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while deleting Job entry: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getJobEntryById(Integer jobEntryId) {
        ResponseDTO response = new ResponseDTO();

        try {
            JobEntry jobEntry = jobEntryRepo.findById(jobEntryId)
                    .orElseThrow(() -> new OurException("Job entry not found"));

            response.setJobEntry(jobEntry);
            response.setStatusCode(200);
            response.setMessage("Successfully Found Job entry");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving Job entry: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getTechnicians(){
        ResponseDTO response = new ResponseDTO();

        try {
            List<OurUsers> result = usersRepo.findByRole( "TECHNICIAN");
            if (!result.isEmpty()){
                response.setOurUsersList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("No Staff Found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Page<JobEntry> findJobEntriesWithPaginationAndSorting(int jobId, int offset) {
        Pageable pageable = PageRequest.of(offset, 10, Sort.by(
                Sort.Order.desc("entryDate"),
                Sort.Order.desc("time")
        ));
        return jobEntryRepo.findByJobRegistry_JobId(jobId, pageable);
    }

    @Override
    public ResponseDTO getAllJobEntriesByJobId(Integer jobId) {
        ResponseDTO response = new ResponseDTO();
        try {
            // Retrieve JobEntry entities by jobId
            List<JobEntry> jobEntries = jobEntryRepo.findByJobRegistry_JobId(jobId);

            if (!jobEntries.isEmpty()) {
                // Map JobEntry entities to JobEntryDTOs
                List<JobEntryDTO> jobEntryDTOs = jobEntries.stream()
                        .map(this::mapToJobEntryDTO)
                        .toList();

                response.setDetails(jobEntryDTOs);
                response.setStatusCode(200);
                response.setMessage("Successfully retrieved job entries.");
            } else {
                throw new OurException("No job entries found for job ID: " + jobId);
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving job entries: " + e.getMessage());
        }

        return response;
    }

    private JobEntryDTO mapToJobEntryDTO(JobEntry jobEntry) {
        JobEntryDTO jobEntryDTO = new JobEntryDTO();
        jobEntryDTO.setJobEntryId(jobEntry.getJobEntryId());
        jobEntryDTO.setJobRegistry(jobEntry.getJobRegistry()); // Map or use ModelMapper if needed
        jobEntryDTO.setEntryDate(jobEntry.getEntryDate());
        jobEntryDTO.setTime(jobEntry.getTime());
        jobEntryDTO.setTechnicianId(jobEntry.getTechnicianId());
        jobEntryDTO.setDetails(jobEntry.getDetails());
        jobEntryDTO.setManHours(jobEntry.getManHours());
        return jobEntryDTO;
    }



    public ResponseDTO reduceItem(int itemId, int quantity) {

        ResponseDTO response = new ResponseDTO();
        try {
            if (inventoryRepo.existsById(itemId)) {
                Inventory inventory = inventoryRepo.getById(itemId);
                if (inventory.getCount() >= quantity) {
                    inventory.setCount(inventory.getCount() - quantity); // Decrease count
                    inventoryRepo.save(inventory);
                    response.setStatusCode(200);
                    response.setMessage("Successfully reduced inventory item.");
                } else {
                    throw new OurException("Insufficient inventory count.");
                }
            } else {
                throw new OurException("No item found under this id.");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while reducing inventory item: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllJobEntriesByUserId(Integer userId){
        ResponseDTO response = new ResponseDTO();
        try {
            // Retrieve JobEntry entities by jobId

            List<Object[]> jobEntryDetails = jobEntryRepo.findEntryWithTechnician(userId);
//            List<JobEntry> jobEntries = jobEntryRepo.findByJobRegistry_JobId(jobId);;
            if (!jobEntryDetails.isEmpty()){
                response.setDetails(jobEntryDetails);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                throw new OurException("No Job entries Found");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving job entries: " + e.getMessage());
        }

        return response;

    }

}
