package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.dashboard.DateRange;
import com.cvconnect.dto.dashboard.admin.*;
import com.cvconnect.dto.enums.EliminateReasonEnumDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAdCandidate.JobAdCandidateProjection;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.entity.JobAd;
import com.cvconnect.enums.EliminateReasonEnum;
import com.cvconnect.repository.DashboardRepository;
import com.cvconnect.service.DashboardService;
import nmquan.commonlib.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    public DashboardOverviewDto getSystemAdminDashboardOverview(DashboardFilter filter) {
        Long numberOfApplications = dashboardRepository.numberOfApplications(filter);
        Long numberOfNewCandidate = restTemplateClient.numberOfNewCandidate(filter);
        Long numberOfNewOrgs = dashboardRepository.numberOfNewOrgs(filter);
        Long numberOfOnboard = dashboardRepository.numberOfOnboard(filter);
        Long numberOfJobAds = dashboardRepository.numberOfJobAds(filter);

        return DashboardOverviewDto.builder()
                .numberOfApplications(numberOfApplications)
                .numberOfNewCandidates(numberOfNewCandidate)
                .numberOfNewOrgs(numberOfNewOrgs)
                .numberOfOnboard(numberOfOnboard)
                .numberOfJobAds(numberOfJobAds)
                .build();
    }

    @Override
    public List<DashboardPercentPassedDto> getPercentPassed(DashboardFilter filter) {
        Map<String, DateRange> dateRangeMap = splitByMonth(filter.getStartTime(), filter.getEndTime());
        List<JobAdCandidateProjection> applyDateData = dashboardRepository.getByApplyDate(filter);
        List<JobAdCandidateProjection> onboardDateData = dashboardRepository.getByOnboard(filter);

        List<DashboardPercentPassedDto> response = new ArrayList<>();
        for(Map.Entry<String, DateRange> entry : dateRangeMap.entrySet()) {
            String key = entry.getKey();
            DateRange dateRange = entry.getValue();

            long applyCount = 0L;
            Long onboardCount = 0L;
            for(JobAdCandidateProjection projection : applyDateData) {
                if(!projection.getApplyDate().isBefore(dateRange.getStartTime()) &&
                   !projection.getApplyDate().isAfter(dateRange.getEndTime())) {
                    applyCount++;
                }
            }
            for(JobAdCandidateProjection projection : onboardDateData) {
                if(!projection.getApplyDate().isBefore(dateRange.getStartTime()) &&
                   !projection.getApplyDate().isAfter(dateRange.getEndTime())) {
                    onboardCount++;
                }
            }
            double percentPassed = applyCount == 0 ? 0.0 : (onboardCount.doubleValue() / (double) applyCount) * 100.0;
            response.add(DashboardPercentPassedDto.builder()
                    .label(key)
                    .numberOfApplications(applyCount)
                    .numberOfPassed(onboardCount)
                    .percent(Math.round(percentPassed * 100.0) / 100.0)
                    .build());
        }
        return response;
    }

    @Override
    public List<DashboardApplyMostDto> getCandidateApplyMost(DashboardFilter filter) {
        List<CandidateInfoApplyProjection> candidateApplies = dashboardRepository.getCandidateApplyMost(filter);

        List<Long> candidateIds = candidateApplies.stream()
                .map(CandidateInfoApplyProjection::getCandidateId)
                .toList();
        Map<Long, UserDto> candidateIdNameMap = restTemplateClient.getUsersByIds(candidateIds);

        List<DashboardApplyMostDto> response = new ArrayList<>();
        for(CandidateInfoApplyProjection projection : candidateApplies) {
            Long candidateId = projection.getCandidateId();
            response.add(DashboardApplyMostDto.builder()
                    .candidateId(candidateId)
                    .candidateName(candidateIdNameMap.get(candidateId) != null ?
                            candidateIdNameMap.get(candidateId).getFullName() : "N/A")
                    .numberOfApplications(projection.getNumOfApply())
                    .build());
        }
        return response;
    }

    @Override
    public List<DashboardEliminatedReasonDto> getPercentEliminatedReason(DashboardFilter filter) {
        List<JobAdCandidateProjection> eliminatedData = dashboardRepository.getEliminatedReasonData(filter)
                .stream()
                .filter(p -> p.getEliminateReasonType() != null)
                .toList();

        int numOfReasons = 0;
        List<DashboardEliminatedReasonDto> response = new ArrayList<>();
        for(JobAdCandidateProjection projection : eliminatedData) {
            EliminateReasonEnumDto reasonEnum = EliminateReasonEnum.getEliminateReasonEnumDto(projection.getEliminateReasonType());
            response.add(DashboardEliminatedReasonDto.builder()
                    .eliminateReason(reasonEnum)
                    .numberOfEliminated(projection.getNumOfApply())
                    .build());
            numOfReasons++;
            if(numOfReasons == 7){
                DashboardEliminatedReasonDto other = new DashboardEliminatedReasonDto();
                other.setEliminateReason(EliminateReasonEnumDto.builder()
                                .description("Các lý do khác")
                        .build());
                other.setNumberOfEliminated(0L);
                for(int i = 7; i < eliminatedData.size(); i++) {
                    projection = eliminatedData.get(i);
                    other.setNumberOfEliminated(other.getNumberOfEliminated() + projection.getNumOfApply());
                }
                if(other.getNumberOfEliminated() > 0) {
                    response.add(other);
                }
                break;
            }
        }
        return response;
    }

    @Override
    public List<DashboardJobAdByTimeDto> getJobAdByTime(DashboardFilter filter) {
        Map<String, DateRange> dateRangeMap = splitByMonth(filter.getStartTime(), filter.getEndTime());
        List<JobAd> jobAds = dashboardRepository.getJobAdByTime(filter);

        List<DashboardJobAdByTimeDto> response = new ArrayList<>();
        for(Map.Entry<String, DateRange> entry : dateRangeMap.entrySet()) {
            String key = entry.getKey();
            DateRange dateRange = entry.getValue();

            Long jobAdCount = 0L;
            for(JobAd jobAd : jobAds) {
                if(!jobAd.getCreatedAt().isBefore(dateRange.getStartTime()) &&
                   !jobAd.getCreatedAt().isAfter(dateRange.getEndTime())) {
                    jobAdCount++;
                }
            }
            response.add(DashboardJobAdByTimeDto.builder()
                    .label(key)
                    .numberOfJobAds(jobAdCount)
                    .build());
        }
        return response;
    }

    @Override
    public List<DashboardJobAdByLevelDto> getJobAdByLevel(DashboardFilter filter) {
        List<Object[]> jobAdByLevelData = dashboardRepository.getJobAdByLevel(filter);
        return jobAdByLevelData.stream()
                .map(p -> {
                    Long levelId = (Long) p[0];
                    String levelName = (String) p[1];
                    Long numOfJobAds = (Long) p[2];
                    return DashboardJobAdByLevelDto.builder()
                            .level(LevelDto.builder()
                                    .id(levelId)
                                    .name(levelName)
                                    .build())
                            .numberOfJobAds(numOfJobAds)
                            .build();
                }).toList();
    }

    @Override
    public List<DashboardJobAdByCareerDto> getJobAdByCareer(DashboardFilter filter) {
        List<Object[]> jobAdByCareerData = dashboardRepository.getJobAdByCareer(filter);
        return jobAdByCareerData.stream()
                .map(p -> {
                    Long careerId = (Long) p[0];
                    String careerName = (String) p[1];
                    Long numOfJobAds = (Long) p[2];
                    double avgSalary = p[3] != null ? ((BigDecimal) p[3]).doubleValue() : 0D;
                    String avgSalaryStr;
                    if(avgSalary == 0) {
                        avgSalaryStr = "N/A";
                    } else {
                        avgSalary = Math.round((avgSalary/1000000) * 10.0) / 10.0;
                        if (avgSalary == 0){
                           avgSalaryStr = "N/A";
                        } else {
                            if(avgSalary % 1 == 0) {
                                avgSalaryStr = String.valueOf((long) avgSalary) + " triệu";
                            } else {
                                avgSalaryStr = avgSalary + " triệu";
                            }
                        }
                    }
                    return DashboardJobAdByCareerDto.builder()
                            .career(CareerDto.builder()
                                    .id(careerId)
                                    .name(careerName)
                                    .build())
                            .numberOfJobAds(numOfJobAds)
                            .avgSalaryStr(avgSalaryStr)
                            .build();
                }).toList();
    }

    @Override
    public List<JobAdDto> getJobAdFeatured(DashboardFilter filter) {
        filter.setPageIndex(0);
        filter.setPageSize(10);
        if (Objects.equals(filter.getSortBy(), CommonConstants.DEFAULT_SORT_BY) ||
                Objects.equals(filter.getSortBy(), CommonConstants.FALLBACK_SORT_BY)) {
            filter.setSortBy("numberOfApplications");
        }
        Page<Object[]> jobAdFeaturedData = dashboardRepository.getJobAdFeatured(filter, filter.getPageable());
        return jobAdFeaturedData.stream()
                .map(p -> {
                    Long id = ((Number) p[0]).longValue();
                    String title = (String) p[1];
                    Long orgId = ((Number) p[2]).longValue();
                    String orgName = (String) p[3];
                    Long numberOfViews = ((Number) p[4]).longValue();
                    Long numberOfApplications = ((Number) p[5]).longValue();
                    return JobAdDto.builder()
                            .id(id)
                            .title(title)
                            .orgId(orgId)
                            .org(OrgDto.builder()
                                    .id(orgId)
                                    .name(orgName)
                                    .build())
                            .numberOfViews(numberOfViews)
                            .numberOfApplications(numberOfApplications)
                            .build();
                }).collect(Collectors.toList());
    }

    public Map<String, DateRange> splitByMonth(Instant startTime, Instant endTime) {
        Map<String, DateRange> result = new LinkedHashMap<>();

        ZonedDateTime start = startTime.atZone(CommonConstants.ZONE.UTC);
        ZonedDateTime end = endTime.atZone(CommonConstants.ZONE.UTC);
        ZonedDateTime pointer = start.withDayOfMonth(1);
        while (!pointer.isAfter(end)) {
            ZonedDateTime monthStart = pointer;
            ZonedDateTime monthEnd = pointer.plusMonths(1).minusSeconds(1);

            Instant blockStart = monthStart.toInstant().isBefore(startTime) ? startTime : monthStart.toInstant();
            Instant blockEnd   = monthEnd.toInstant().isAfter(endTime) ? endTime : monthEnd.toInstant();

            String key = "T" + pointer.getMonthValue() + "-" + pointer.getYear();

            result.put(key, new DateRange(blockStart, blockEnd));

            pointer = pointer.plusMonths(1);
        }
        return result;
    }

}
