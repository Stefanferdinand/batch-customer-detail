package batch.customer.detail.controller;

import batch.customer.detail.configuration.CustomerConfig;
import batch.customer.detail.configuration.RetryJobConfig;
import batch.customer.detail.configuration.TransactionConfig;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchCustomerDetailController {

    private RetryJobConfig retryJobConfig;
    private CustomerConfig customerConfig;
    private TransactionConfig transactionConfig;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;

    public BatchCustomerDetailController(RetryJobConfig retryJobConfig, CustomerConfig customerConfig, TransactionConfig transactionConfig, JobRepository jobRepository, JobLauncher jobLauncher) {
        this.retryJobConfig = retryJobConfig;
        this.customerConfig = customerConfig;
        this.transactionConfig = transactionConfig;
        this.jobRepository = jobRepository;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/retryAllFailedJob")
    public String retryAllFailedJob(){
        retryJobConfig.retryJob();
        return "Retry Failed Jobs";
    }

//    @GetMapping("/retryFailedJob")
//    public String retryFailedJob(@RequestParam long instanceId){
//        jobLauncher.run()
//    }

//    @GetMapping("/triggerCustomerJob")
//    public String triggerManualJob(){
//
//        JobParameters parameters = new JobParametersBuilder()
//                .addString("status", "manual")
//                .toJobParameters();
//
//        jobRepository.getJobInstance(jobName, parameters);
//
//        jobLauncher.run()
//    }

//    @GetMapping("/triggerTransactionJob")
//    public String triggerManualJob(){
//
//        JobParameters parameters = new JobParametersBuilder()
//                .addString("status", "manual")
//                .toJobParameters();
//
//        jobRepository.getJobInstance(jobName, parameters);
//
//        jobLauncher.run()
//    }
}