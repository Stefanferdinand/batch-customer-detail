package batch.customer.detail.controller;

import batch.customer.detail.configuration.JobConfiguration;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchCustomerDetailController {

    private JobConfiguration jobConfiguration;
    private JobLauncher jobLauncher;

    public BatchCustomerDetailController(JobConfiguration jobConfiguration, JobLauncher jobLauncher) {
        this.jobConfiguration = jobConfiguration;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/retryFailedJobs")
    public String retryFailedJobs(){
        JobParameters parameters = new JobParametersBuilder()
                .addString("job", "all")
                .toJobParameters();

        // nunggu model
//        jobLauncher.run(jobConfiguration.job());

        return "Retry Failed Job";
    }

}
