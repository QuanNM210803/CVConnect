package com.cvconnect.repository;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.dashboard.admin.DashboardFilter;
import com.cvconnect.dto.dashboard.org.OrgAdminDashboardFilter;
import com.cvconnect.dto.jobAdCandidate.JobAdCandidateProjection;
import com.cvconnect.entity.JobAd;
import com.cvconnect.entity.Organization;
import nmquan.commonlib.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<BaseEntity, Long> {
    @Query("""
            select count(distinct jac) from JobAdCandidate jac
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
    """)
    Long numberOfApplications(DashboardFilter filter);

    @Query("""
            select count(distinct o) from Organization o
            where o.createdAt between :#{#filter.startTime} and :#{#filter.endTime}
    """)
    Long numberOfNewOrgs(DashboardFilter filter);

    @Query("""
            select count(distinct jac) from JobAdCandidate jac
            where jac.onboardDate between :#{#filter.startTime} and :#{#filter.endTime}
            and jac.candidateStatus IN ('ONBOARDED')
    """)
    Long numberOfOnboard(DashboardFilter filter);

    @Query("""
            select count(distinct ja) from JobAd ja
            where ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
    """)
    Long numberOfJobAds(DashboardFilter filter);

    @Query("""
            select distinct jac.id as id, jac.applyDate as applyDate
            from JobAdCandidate jac
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
    """)
    List<JobAdCandidateProjection> getByApplyDate(DashboardFilter filter);

    @Query("""
            select distinct jac.id as id, jac.applyDate as applyDate
            from JobAdCandidate jac
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
            and jac.candidateStatus IN ('ONBOARDED')
    """)
    List<JobAdCandidateProjection> getByOnboard(DashboardFilter filter);

    @Query("""
        select cia.candidateId as candidateId, count(distinct jac.id) as numOfApply
        from JobAdCandidate jac
        join CandidateInfoApply cia on jac.candidateInfoId = cia.id
        where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
        group by cia.candidateId
        order by numOfApply desc
        limit 10
    """)
    List<CandidateInfoApplyProjection> getCandidateApplyMost(DashboardFilter filter);

    @Query("""
        select jac.eliminateReasonType as eliminateReasonType, count(distinct jac.id) as numOfApply
        from JobAdCandidate jac
        where jac.eliminateDate between :#{#filter.startTime} and :#{#filter.endTime}
        and jac.candidateStatus = 'REJECTED'
        group by jac.eliminateReasonType
        order by numOfApply desc
    """)
    List<JobAdCandidateProjection> getEliminatedReasonData(DashboardFilter filter);

    @Query("""
        select ja from JobAd ja
        where ja.createdAt between :#{#filter.startTime} and :#{#filter.endTime}
    """)
    List<JobAd> getJobAdByTime(DashboardFilter filter);

    @Query(value = """
        with count as (
            select l.id as id, l.name as name, count(distinct ja.id) as numOfJobAds
            from job_ad ja
            join job_ad_level jal on jal.job_ad_id= ja.id
            join level l on l.id = jal.level_id
            where ja.created_at <= :#{#filter.endTime} and ja.due_date >= :#{#filter.startTime}
            and ja.is_all_level = false
            group by l.id, l.name
            union all
            select null as id, 'Tất cả cấp bậc' as name, count(distinct ja.id) as numOfJobAds
            from job_ad ja
            where ja.created_at <= :#{#filter.endTime} and ja.due_date >= :#{#filter.startTime}
            and ja.is_all_level = true
            group by ja.is_all_level
        )
        select * from count
        order by numOfJobAds desc
    """, nativeQuery = true)
    List<Object[]> getJobAdByLevel(DashboardFilter filter);

    @Query(value = """
        SELECT
            c.id AS careerId,
            c.name AS careerName,
            COUNT(DISTINCT jac.job_ad_id) AS numOfJobAds,
            AVG(
                CASE
                    WHEN ja.salary_type = 'RANGE' THEN (ja.salary_from + ja.salary_to) / 2
                    ELSE NULL
                END
            ) AS avgSalary
        FROM careers c
        JOIN job_ad_career jac ON c.id = jac.career_id
        JOIN job_ad ja ON ja.id = jac.job_ad_id
        where ja.created_at <= :#{#filter.endTime} and ja.due_date >= :#{#filter.startTime}
        GROUP BY c.id, c.name
        ORDER BY numOfJobAds DESC
        LIMIT 15
    """, nativeQuery = true)
    List<Object[]> getJobAdByCareer(DashboardFilter filter);

    @Query(value = """
        select ja.id as id, ja.title as title, ja.org_id as orgId, o.name as orgName,
               case when jas.view_count is null then 0 else jas.view_count end as numberOfViews,
               count(distinct jac.id) as numberOfApplications
        from job_ad ja
        left join job_ad_statistic jas on jas.job_ad_id = ja.id
        left join job_ad_candidate jac on jac.job_ad_id = ja.id
        join organization o on o.id = ja.org_id
        where ja.created_at <= :#{#filter.endTime} and ja.due_date >= :#{#filter.startTime}
        and (coalesce(:#{#filter.orgId}, null) is null or ja.org_id = :#{#filter.orgId})
        group by ja.id, ja.title, ja.org_id, o.name, case when jas.view_count is null then 0 else jas.view_count end
    """, nativeQuery = true)
    Page<Object[]> getJobAdFeatured(DashboardFilter filter, Pageable pageable);

    @Query("""
            select distinct o from Organization o
            where o.createdAt between :#{#filter.startTime} and :#{#filter.endTime}
    """)
    List<Organization> getNewOrgByTime(DashboardFilter filter);

    @Query("""
        select
            case
                when o.staffCountFrom is not null and o.staffCountTo is not null then concat(o.staffCountFrom, '-', o.staffCountTo)
                when o.staffCountFrom is not null and o.staffCountTo is null then concat(o.staffCountFrom, '+')
                when o.staffCountFrom is null and o.staffCountTo is not null then concat('Up to ', o.staffCountTo)
                else 'N/A'
            end as staffSize,
            count(distinct o.id) as numberOfOrgs
        from Organization o
        group by
            case
                when o.staffCountFrom is not null and o.staffCountTo is not null then concat(o.staffCountFrom, '-', o.staffCountTo)
                when o.staffCountFrom is not null and o.staffCountTo is null then concat(o.staffCountFrom, '+')
                when o.staffCountFrom is null and o.staffCountTo is not null then concat('Up to ', o.staffCountTo)
                else 'N/A'
            end
        order by numberOfOrgs desc
    """)
    List<Object[]> getOrgStaffSize();

    @Query(value = """
        select o.id as id, o.name as orgName, o.logoId as logoId,
               count(distinct ja.id) as numberOfJobAds,
               sum(
                   case
                       when jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numberOfApplications,
               sum(
                   case
                       when jac.candidateStatus = 'ONBOARDED' and jac.onboardDate between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numberOfOnboarded
        from Organization o
        left join JobAd ja on ja.orgId = o.id and ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
        left join JobAdCandidate jac on jac.jobAdId = ja.id
        where (:#{#filter.orgName} is null or lower(o.name) like lower(concat('%', :#{#filter.orgName}, '%')))
        group by o.id, o.name, o.logoId
    """,
    countQuery = """
        select distinct o.id as id
        from Organization o
        left join JobAd ja on ja.orgId = o.id and ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
        left join JobAdCandidate jac on jac.jobAdId = ja.id
        where (:#{#filter.orgName} is null or lower(o.name) like lower(concat('%', :#{#filter.orgName}, '%')))
        group by o.id, o.name, o.logoId
    """)
    Page<Object[]> getOrgFeatured(DashboardFilter filter, Pageable pageable);

    @Query("""
            select count(distinct ja) from JobAd ja
            where ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
            and ja.orgId = :#{#filter.orgId}
    """)
    Long numberOfJobAds(OrgAdminDashboardFilter filter);

    @Query("""
            select count(distinct ja) from JobAd ja
            where ja.jobAdStatus = 'OPEN'
            and ja.orgId = :#{#filter.orgId}
    """)
    Long numberOfOpenJobAds(OrgAdminDashboardFilter filter);

    @Query("""
            select count(distinct jac) from JobAdCandidate jac
            join JobAd ja on jac.jobAdId = ja.id
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
            and ja.orgId = :#{#filter.orgId}
    """)
    Long numberOfApplications(OrgAdminDashboardFilter filter);

    @Query("""
            select count(distinct jac) from JobAdCandidate jac
            join JobAd ja on jac.jobAdId = ja.id
            where jac.candidateStatus IN ('VIEWED_CV', 'IN_PROGRESS')
            and ja.orgId = :#{#filter.orgId}
    """)
    Long numberOfCandidateInProcess(OrgAdminDashboardFilter filter);

    @Query("""
            select count(distinct jac) from JobAdCandidate jac
            join JobAd ja on jac.jobAdId = ja.id
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
            and jac.candidateStatus IN ('ONBOARDED')
            and ja.orgId = :#{#filter.orgId}
    """)
    Long numberOfOnboard(OrgAdminDashboardFilter filter);

    @Query("""
            select distinct jac.id as id, jac.applyDate as applyDate
            from JobAdCandidate jac
            join JobAd ja on jac.jobAdId = ja.id
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
            and ja.orgId = :#{#filter.orgId}
    """)
    List<JobAdCandidateProjection> getByApplyDate(OrgAdminDashboardFilter filter);

    @Query("""
            select distinct jac.id as id, jac.applyDate as applyDate
            from JobAdCandidate jac
            join JobAd ja on jac.jobAdId = ja.id
            where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
            and jac.candidateStatus IN ('ONBOARDED')
            and ja.orgId = :#{#filter.orgId}
    """)
    List<JobAdCandidateProjection> getByOnboard(OrgAdminDashboardFilter filter);

    @Query("""
        select ja.hrContactId as hrContactId, count(distinct ja.id) as numOfJobAds,
               sum(
                   case
                       when jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numberOfApplications,
               sum(
                   case
                       when jac.candidateStatus IN ('ONBOARDED') and jac.onboardDate between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numOfOnboarded
        from JobAd ja
        left join JobAdCandidate jac on ja.id = jac.jobAdId
        where ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
        and ja.orgId = :#{#filter.orgId}
        group by ja.hrContactId
        order by numOfJobAds desc
    """)
    List<Object[]> getOrgAdminJobAdByHr(OrgAdminDashboardFilter filter);

    @Query("""
        select d.code as departmentCode, d.name as departmentName,
               count(distinct ja.id) as numOfJobAds,
               sum(
                   case
                       when jac.candidateStatus IN ('ONBOARDED') and jac.onboardDate between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numOfOnboarded
        from Department d
        join Position p on p.departmentId = d.id
        join JobAd ja on ja.positionId = p.id
        left join JobAdCandidate jac on ja.id = jac.jobAdId
        where ja.createdAt <= :#{#filter.endTime} and ja.dueDate >= :#{#filter.startTime}
        and ja.orgId = :#{#filter.orgId}
        group by d.code, d.name
        order by numOfJobAds desc
    """)
    List<Object[]> getOrgAdminJobAdByDepartment(OrgAdminDashboardFilter filter);

    @Query("""
        select case when l.name is null then 'Không xác định' else l.name end as level,
            count(distinct jac.id) as totalOnboarded
        from JobAdCandidate jac
        join CandidateInfoApply cia on cia.id = jac.candidateInfoId
        join JobAd ja on jac.jobAdId = ja.id
        left join CandidateSummaryOrg cso on cso.candidateInfoId = cia.id and cso.orgId = ja.orgId
        left join Level l on l.id = cso.levelId
        where jac.applyDate between :#{#filter.startTime} and :#{#filter.endTime}
        and jac.candidateStatus IN ('ONBOARDED')
        and ja.orgId = :#{#filter.orgId}
        group by case when l.name is null then 'Không xác định' else l.name end
        order by totalOnboarded desc
    """)
    List<Object[]> getOrgAdminPassByLevel(OrgAdminDashboardFilter filter);

    @Query("""
        select jac.eliminateReasonType as eliminateReasonType, count(distinct jac.id) as numOfApply
        from JobAdCandidate jac
        join JobAd ja on jac.jobAdId = ja.id
        where jac.eliminateDate between :#{#filter.startTime} and :#{#filter.endTime}
        and jac.candidateStatus = 'REJECTED'
        and ja.orgId = :#{#filter.orgId}
        group by jac.eliminateReasonType
        order by numOfApply desc
    """)
    List<JobAdCandidateProjection> getEliminatedReasonData(OrgAdminDashboardFilter filter);

    @Query(value = """
        select ja.id as id, ja.title as title,
               case when jas.view_count is null then 0 else jas.view_count end as numberOfViews,
               sum(
                   case
                       when jac.apply_date between :#{#filter.startTime} and :#{#filter.endTime}
                       then 1
                       else 0
                   end
               ) as numberOfApplications,
        from job_ad ja
        left join job_ad_statistic jas on jas.job_ad_id = ja.id
        left join job_ad_candidate jac on jac.job_ad_id = ja.id
        where ja.created_at <= :#{#filter.endTime} and ja.due_date >= :#{#filter.startTime}
        and ja.org_id = :#{#filter.orgId}
        group by ja.id, ja.title, case when jas.view_count is null then 0 else jas.view_count end
    """, nativeQuery = true)
    Page<Object[]> getJobAdFeatured(OrgAdminDashboardFilter filter, Pageable pageable);
}
