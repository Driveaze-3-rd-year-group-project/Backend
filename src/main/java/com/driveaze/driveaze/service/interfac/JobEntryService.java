package com.driveaze.driveaze.service.interfac;

import com.driveaze.driveaze.dto.JobEntryDTO;
import com.driveaze.driveaze.dto.ResponseDTO;
import com.driveaze.driveaze.entity.JobEntry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobEntryService {
    ResponseDTO addNewJobEntry(JobEntryDTO jobEntryDTO);

    ResponseDTO getAllEntriesOfJob(Integer jobId);

    ResponseDTO updateJobEntry(Integer jobEntryId, JobEntryDTO jobEntryDTO);

    ResponseDTO deleteJobEntry(Integer jobEntryId);

    ResponseDTO getJobEntryById(Integer jobEntryId);

    ResponseDTO getTechnicians();

    Page<JobEntry> findJobEntriesWithPaginationAndSorting(int jobId,int offset);

    ResponseDTO getAllJobEntriesByJobId(Integer jobId);

    ResponseDTO getAllJobEntriesByUserId(Integer userId);

}
