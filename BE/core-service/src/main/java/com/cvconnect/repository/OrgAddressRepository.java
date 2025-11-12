package com.cvconnect.repository;

import com.cvconnect.dto.org.OrgAddressProjection;
import com.cvconnect.entity.OrganizationAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("DELETE FROM OrganizationAddress oa WHERE oa.id IN :ids")
    void deleteByIds(List<Long> ids);

    @Query("SELECT DISTINCT oa FROM OrganizationAddress oa " +
            "JOIN JobAdWorkLocation jawl ON jawl.workLocationId = oa.id " +
            "WHERE jawl.jobAdId = :jobAdId")
    List<OrganizationAddress> findByJobAdId(Long jobAdId);

    @Query("""
        SELECT distinct
            oa.id AS id,
            oa.orgId AS orgId,
            oa.isHeadquarter AS isHeadquarter,
            oa.province AS province,
            oa.district AS district,
            oa.ward AS ward,
            oa.detailAddress AS detailAddress,
            jawl.jobAdId AS jobAdId
        FROM OrganizationAddress oa
        JOIN JobAdWorkLocation jawl ON jawl.workLocationId = oa.id
        WHERE jawl.jobAdId IN :jobAdIds
    """)
    List<OrgAddressProjection> findByJobAdIdIn(List<Long> jobAdIds);


}
