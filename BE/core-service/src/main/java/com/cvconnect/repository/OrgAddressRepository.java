package com.cvconnect.repository;

import com.cvconnect.entity.OrganizationAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgAddressRepository extends JpaRepository<OrganizationAddress, Long> {
    @Query("SELECT oa FROM OrganizationAddress oa WHERE oa.orgId = :orgId AND oa.id IN :ids")
    List<OrganizationAddress> findByOrgIdAndIdIn(Long orgId, List<Long> ids);

    @Query("SELECT oa FROM OrganizationAddress oa WHERE oa.orgId = :orgId " +
            "ORDER BY oa.isHeadquarter DESC, oa.createdAt DESC")
    List<OrganizationAddress> findByOrgId(Long orgId);
}
