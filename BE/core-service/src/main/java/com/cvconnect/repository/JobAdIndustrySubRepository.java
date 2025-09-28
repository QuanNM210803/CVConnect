package com.cvconnect.repository;

import com.cvconnect.entity.JobAdIndustrySub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdIndustrySubRepository extends JpaRepository<JobAdIndustrySub, Long> {
}
