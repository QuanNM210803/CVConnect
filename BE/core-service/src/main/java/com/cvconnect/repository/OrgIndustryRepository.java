package com.cvconnect.repository;

import com.cvconnect.entity.OrganizationIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgIndustryRepository extends JpaRepository<OrganizationIndustry, Long> {

    @Modifying
    @Query("DELETE FROM OrganizationIndustry oi WHERE oi.orgId = :orgId")
    void deleteByOrgId(Long orgId);

    @Modifying
    @Query("DELETE FROM OrganizationIndustry oi WHERE oi.industryId IN :industryIds AND oi.orgId = :orgId")
    void deleteByIndustryIdsAndOrgId(List<Long> industryIds, Long orgId);

}
