package com.driveaze.driveaze.controller;

import com.driveaze.driveaze.dto.JobEntryDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.entity.JobEntry;
import com.driveaze.driveaze.service.interfac.JobEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-entry")
@CrossOrigin
public class JobEntryController {
    @Autowired
    private JobEntryService jobEntryService;

    @PostMapping(path = "/save" )
    public ResponseEntity<ResponseDTO> addNewJobEntry(@RequestBody JobEntryDTO jobEntryDTO){
        return ResponseEntity.ok(jobEntryService.addNewJobEntry(jobEntryDTO));
    }

    @PutMapping(path = "/update/{jobEntryId}")
    public ResponseEntity<ResponseDTO> updateJobEntry(@PathVariable Integer jobEntryId, @RequestBody JobEntryDTO jobEntryDTO) {
        ResponseDTO responseDTO = jobEntryService.updateJobEntry(jobEntryId, jobEntryDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/delete/{jobEntryId}")
    public ResponseEntity<ResponseDTO> deleteJobEntry(@PathVariable Integer jobEntryId) {
        return ResponseEntity.ok(jobEntryService.deleteJobEntry(jobEntryId));
    }

    @GetMapping("/get-all-entries-of-job/{jobId}")
    public ResponseEntity<ResponseDTO> getAllEntriesOfJob(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobEntryService.getAllEntriesOfJob(jobId));
    }

    @GetMapping("/get-job-entry/{jobEntryId}")
    public ResponseEntity<ResponseDTO> getJobEntryById(@PathVariable Integer jobEntryId) {
        return ResponseEntity.ok(jobEntryService.getJobEntryById(jobEntryId));
    }

    @GetMapping("/get-technicians")
    public ResponseEntity<ResponseDTO> getTechnicians() {
        return ResponseEntity.ok(jobEntryService.getTechnicians());
    }

    @GetMapping("/paginationAndSort/{jobId}/{offset}")
    public Page<JobEntry> findJobEntriesWithPaginationAndSorting(@PathVariable Integer jobId, @PathVariable int offset) {
        return jobEntryService.findJobEntriesWithPaginationAndSorting(jobId, offset);
    }

    @GetMapping("/get-all-job-entries-by-job-id/{jobId}")
    public ResponseEntity<ResponseDTO> getAllJobEntriesbyJobId(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobEntryService.getAllJobEntriesByJobId(jobId));
    }

    @GetMapping("/get-all--entries-of-technician/{userId}")
    public ResponseEntity<ResponseDTO> getAllJobEntriesbyUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(jobEntryService.getAllJobEntriesByUserId(userId));
    }

}
