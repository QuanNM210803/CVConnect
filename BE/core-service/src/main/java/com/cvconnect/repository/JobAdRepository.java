package com.cvconnect.repository;

import com.cvconnect.entity.JobAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Integer> {
}
