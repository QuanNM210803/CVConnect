package com.cvconnect.repository;

import com.cvconnect.entity.OrganizationIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgIndustryRepository extends JpaRepository<OrganizationIndustry, Long> {

}
