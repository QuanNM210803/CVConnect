package com.cvconnect.repository;

import com.cvconnect.entity.JobAdProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdProcessRepository extends JpaRepository<JobAdProcess, Long> {
    @Query("SELECT j FROM JobAdProcess j WHERE j.jobAdId = :jobAdId")
    List<JobAdProcess> findByJobAdId(Long jobAdId);
}
