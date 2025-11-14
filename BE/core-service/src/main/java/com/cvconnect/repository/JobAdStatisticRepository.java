package com.cvconnect.repository;

import com.cvconnect.entity.JobAdStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdStatisticRepository extends JpaRepository<JobAdStatistic, Long> {
}
