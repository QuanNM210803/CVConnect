package com.cvconnect.repository;

import com.cvconnect.entity.JobAdProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdProcessRepository extends JpaRepository<JobAdProcess, Long> {
}
