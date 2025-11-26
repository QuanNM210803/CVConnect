package com.cvconnect.repository;

import com.cvconnect.entity.JobAdWorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdWorkLocationRepository extends JpaRepository<JobAdWorkLocation, Long> {
}
