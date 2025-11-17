package com.cvconnect.repository;

import com.cvconnect.dto.org.OrgProjection;
import com.cvconnect.entity.Organization;
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
}
