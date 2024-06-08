package batch.customer.detail.controller;

import batch.customer.detail.configuration.RetryJobConfig;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class BatchCustomerDetailController {

    private RetryJobConfig retryJobConfig;
    private JobLauncher jobLauncher;

    public BatchCustomerDetailController(RetryJobConfig retryJobConfig, JobLauncher jobLauncher) {
        this.retryJobConfig = retryJobConfig;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/retryFailedJobs")
    public String retryFailedJobs(){
        JobParameters parameters = new JobParametersBuilder()
                .addString("job", new Date().toString())
                .toJobParameters();

        try {
            jobLauncher.run(retryJobConfig.retryAllFailedJobs(), parameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Retry Failed Job";
    }
}