package com.cvconnect.job;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.common.JobConfigDto;
import com.cvconnect.service.JobConfigService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nmquan.commonlib.job.RunningJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableScheduling
public class JobScheduler {
    @Autowired
    @Qualifier(Constants.BeanName.TASK_SCHEDULER)
    private TaskScheduler taskScheduler;
    @Autowired
    private JobConfigService jobConfigService;
    private final Map<String, RunningJob> jobRegistry;

    public JobScheduler(List<RunningJob> jobs) {
        this.jobRegistry = jobs.stream()
                .collect(Collectors.toMap(RunningJob::getJobName, Function.identity()));
    }

    @PostConstruct
    public void scheduleJobsFromDb() {
        List<JobConfigDto> jobConfigs = jobConfigService.getAllJobs();
        for (JobConfigDto config : jobConfigs) {
            RunningJob job = jobRegistry.get(config.getJobName());
            if (job == null) {
                continue;
            }
            switch (config.getScheduleType()) {
                case CRON:
                    taskScheduler.schedule(job::runJob, new CronTrigger(config.getExpression()));
                    break;
                case FIXED_RATE:
                    taskScheduler.scheduleAtFixedRate(job::runJob, Long.parseLong(config.getExpression()) * 1000);
                    break;
                case FIXED_DELAY:
                    taskScheduler.scheduleWithFixedDelay(job::runJob, Long.parseLong(config.getExpression()) * 1000);
                    break;
            }
            log.info("⏰ Scheduled job: {} [{} - {}s]", config.getJobName(), config.getScheduleType(), config.getExpression());
        }
    }
}
