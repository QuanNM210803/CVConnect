package com.cvconnect.repository;

import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.dto.industry.IndustryProjection;
import com.cvconnect.entity.Industry;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {

    @Query("SELECT DISTINCT i FROM Industry i " +
            "WHERE (:#{#request.code} IS NULL OR LOWER(i.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
            "AND (:#{#request.name} IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
            "AND (i.createdAt >= COALESCE(:#{#request.createdAtStart}, i.createdAt)) " +
            "AND (i.createdAt <= COALESCE(:#{#request.createdAtEnd}, i.createdAt)) " +
            "AND (COALESCE(:#{#request.updatedAtStart}, NULL) IS NULL OR (i.updatedAt IS NOT NULL AND i.updatedAt >= :#{#request.updatedAtStart})) " +
            "AND (COALESCE(:#{#request.updatedAtEnd}, NULL) IS NULL OR (i.updatedAt IS NOT NULL AND i.updatedAt <= :#{#request.updatedAtEnd})) " +
            "AND (:#{#request.createdBy} IS NULL OR LOWER(i.createdBy) LIKE LOWER(CONCAT('%', :#{#request.createdBy}, '%'))) " +
            "AND (:#{#request.updatedBy} IS NULL OR LOWER(i.updatedBy) LIKE LOWER(CONCAT('%', :#{#request.updatedBy}, '%'))) "
    )
    Page<Industry> filter(IndustryFilterRequest request, Pageable pageable);

    boolean existsByCode(@Size(max = 50) String code);

    Industry findByCode(@Size(max = 50) String code);

    @Query("SELECT DISTINCT i FROM Industry i " +
            "JOIN OrganizationIndustry oi ON oi.industryId = i.id " +
            "WHERE oi.orgId = :orgId")
    List<Industry> getIndustriesByOrgId(Long orgId);

    @Query("""
        select distinct i.id as id,
                        i.code as code,
                        i.name as name,
                        i.description as description,
                        oi.orgId as orgId
        from OrganizationIndustry oi
        join Industry i on oi.industryId = i.id
        where oi.orgId in :orgIds
    """)
    List<IndustryProjection> getIndustriesByOrgIds(List<Long> orgIds);
}
