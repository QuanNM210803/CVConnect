package com.cvconnect.repository;

import com.cvconnect.entity.JobAdLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdLevelRepository extends JpaRepository<JobAdLevel, Long> {
}
