package com.cvconnect.repository;

import com.cvconnect.entity.JobAdCareer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdCareerRepository extends JpaRepository<JobAdCareer, Long> {
}
