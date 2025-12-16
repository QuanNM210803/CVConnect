package com.cvconnect.job;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.dto.failedRollback.FailedRollbackOrgCreation;
import com.cvconnect.enums.FailedRollbackType;
import com.cvconnect.service.FailedRollbackService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import nmquan.commonlib.job.RunningJob;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FailedRollbackRetryJob implements RunningJob {
    @Autowired
    private FailedRollbackService failedRollbackService;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Override
    public String getJobName() {
        return Constants.JobName.FAILED_ROLLBACK_RETRY;
    }

    @Override
    public String getScheduleType() {
        return "";
    }

    @Override
    public String getExpression() {
        return "";
    }

    @Override
    @SchedulerLock(name = Constants.JobName.FAILED_ROLLBACK_RETRY, lockAtMostFor = "5m", lockAtLeastFor = "2m")
    public void runJob() {
        log.info("\uD83D\uDE80 Running job: {}", getJobName());
        List<FailedRollbackDto> failedRollbacks = failedRollbackService.getPendingFailedRollbacks();
        for(FailedRollbackDto dto : failedRollbacks) {
            if(FailedRollbackType.ORG_CREATION.getType().equals(dto.getType())) {
                try{
                    FailedRollbackOrgCreation orgCreation = ObjectMapperUtils.convertToObject(dto, FailedRollbackOrgCreation.class);
                    restTemplateClient.deleteOrg(orgCreation);
                    dto.setStatus(true);
                    failedRollbackService.save(dto);
                    log.info("✅ Successfully rolled back org creation for FailedRollback id: {}", dto.getId());
                } catch (Exception ex){
                    dto.setRetryCount(dto.getRetryCount() + 1);
                    failedRollbackService.save(dto);
                    log.error("❌ Failed to rollback org creation for FailedRollback id: {}. Error: {}", dto.getId(), ex.getMessage());
                }
            }
        }
        log.info("✅ Finished job: {}", getJobName());
    }
}
