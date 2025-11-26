package com.cvconnect.repository;

import com.cvconnect.entity.JobAdStatistic;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobAdStatisticRepository extends JpaRepository<JobAdStatistic, Long> {
    @Query("SELECT j FROM JobAdStatistic j WHERE j.jobAdId = :jobAdId")
    JobAdStatistic findByJobAdId(Long jobAdId);
}
