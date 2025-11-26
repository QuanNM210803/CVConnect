package com.cvconnect.repository;

import com.cvconnect.dto.org.OrgFilterRequest;
import com.cvconnect.dto.org.OrgProjection;
import com.cvconnect.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Long> {
    @Modifying
    @Query("UPDATE Organization o SET o.createdBy = :createdBy WHERE o.id = :orgId")
    void updateCreatedBy(Long orgId, String createdBy);

    @Query(value = """
        with org as (
            select o.id,
                o.created_at,
                count(distinct jac.id) as number_of_applicants,
                count(distinct ja.id) as number_of_job_ads
            from organization o
            left join job_ad ja on ja.org_id = o.id
            left join job_ad_candidate jac on jac.job_ad_id = ja.id
            where o.is_active = true
            and ja.is_public = true
            group by o.id, o.created_at
            order by number_of_applicants desc, number_of_job_ads desc, o.created_at desc
            limit 10
        )
        select o.id as id,
                o.name as name,
                o.description as description,
                o.logo_id as logoId,
                o.cover_photo_id as coverPhotoId,
                o.website as website,
                o.staff_count_from as staffCountFrom,
                o.staff_count_to as staffCountTo,
                number_of_job_ads as numOfJobAds
        from org
        join organization o on o.id = org.id;
    """, nativeQuery = true)
    List<OrgProjection> findFeaturedOrgs();

    // ko sort staffCountFrom, staffCountTo
    @Query(value = """
        select distinct o.id as id,
                o.name as name,
                o.website as website,
                o.staffCountFrom as staffCountFrom,
                o.staffCountTo as staffCountTo,
                o.isActive as isActive,
                o.createdAt as createdAt,
                o.updatedAt as updatedAt,
                o.createdBy as createdBy,
                o.updatedBy as updatedBy
        from Organization o
        left join OrganizationAddress oa on oa.orgId = o.id
        left join OrganizationIndustry oi on oi.orgId = o.id
        where (:#{#request.orgName} is null or lower(o.name) like lower(concat('%', :#{#request.orgName}, '%')))
        and (:#{#request.website} is null or lower(o.website) like lower(concat('%', :#{#request.website}, '%')))
        and (:#{#request.staffCountFrom} is null or o.staffCountFrom >= :#{#request.staffCountFrom})
        and (:#{#request.staffCountTo} is null or o.staffCountTo <= :#{#request.staffCountTo})
        and (coalesce(:#{#request.addresses}, null) is null or oa.province in :#{#request.addresses})
        and (coalesce(:#{#request.industryIds}, null) is null or oi.industryId in :#{#request.industryIds})
        and (:#{#request.isActive} is null or o.isActive = :#{#request.isActive})
        and (o.createdAt >= coalesce(:#{#request.createdAtStart}, o.createdAt))
        and (o.createdAt <= coalesce(:#{#request.createdAtEnd}, o.createdAt))
        and (coalesce(:#{#request.updatedAtStart}, null) is null or (o.updatedAt is not null and o.updatedAt >= :#{#request.updatedAtStart}))
        and (coalesce(:#{#request.updatedAtEnd}, null) is null or (o.updatedAt is not null and o.updatedAt <= :#{#request.updatedAtEnd}))
        and (:#{#request.createdBy} is null or lower(o.createdBy) like lower(concat('%', :#{#request.createdBy}, '%')))
        and (:#{#request.updatedBy} is null or lower(o.updatedBy) like lower(concat('%', :#{#request.updatedBy}, '%')))
    """,
    countQuery = """
        select count(distinct o.id)
        from Organization o
        left join OrganizationAddress oa on oa.orgId = o.id
        left join OrganizationIndustry oi on oi.orgId = o.id
        where (:#{#request.orgName} is null or lower(o.name) like lower(concat('%', :#{#request.orgName}, '%')))
        and (:#{#request.website} is null or lower(o.website) like lower(concat('%', :#{#request.website}, '%')))
        and (:#{#request.staffCountFrom} is null or o.staffCountFrom >= :#{#request.staffCountFrom})
        and (:#{#request.staffCountTo} is null or o.staffCountTo <= :#{#request.staffCountTo})
        and (coalesce(:#{#request.addresses}, null) is null or oa.province in :#{#request.addresses})
        and (coalesce(:#{#request.industryIds}, null) is null or oi.industryId in :#{#request.industryIds})
        and (:#{#request.isActive} is null or o.isActive = :#{#request.isActive})
        and (o.createdAt >= coalesce(:#{#request.createdAtStart}, o.createdAt))
        and (o.createdAt <= coalesce(:#{#request.createdAtEnd}, o.createdAt))
        and (coalesce(:#{#request.updatedAtStart}, null) is null or (o.updatedAt is not null and o.updatedAt >= :#{#request.updatedAtStart}))
        and (coalesce(:#{#request.updatedAtEnd}, null) is null or (o.updatedAt is not null and o.updatedAt <= :#{#request.updatedAtEnd}))
        and (:#{#request.createdBy} is null or lower(o.createdBy) like lower(concat('%', :#{#request.createdBy}, '%')))
        and (:#{#request.updatedBy} is null or lower(o.updatedBy) like lower(concat('%', :#{#request.updatedBy}, '%')))
    """
    )
    Page<OrgProjection> filterOrgs(OrgFilterRequest request, Pageable pageable);

    @Modifying
    @Query("UPDATE Organization o SET o.isActive = :isActive WHERE o.id IN :ids")
    void updateStatus(List<Long> ids, Boolean isActive);
}
