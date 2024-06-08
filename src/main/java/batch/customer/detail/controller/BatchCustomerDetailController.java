package batch.customer.detail.controller;

import batch.customer.detail.configuration.CustomerConfig;
import batch.customer.detail.configuration.RetryJobConfig;
import batch.customer.detail.configuration.TransactionConfig;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class BatchCustomerDetailController {

    private RetryJobConfig retryJobConfig;
    private CustomerConfig customerConfig;
    private TransactionConfig transactionConfig;
    private JobLauncher jobLauncher;

    public BatchCustomerDetailController(RetryJobConfig retryJobConfig,
             CustomerConfig customerConfig,
             TransactionConfig transactionConfig,
             JobLauncher jobLauncher) {
        this.retryJobConfig = retryJobConfig;
        this.customerConfig = customerConfig;
        this.transactionConfig = transactionConfig;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/retryAllFailedJob")
    public String retryAllFailedJob(){
        retryJobConfig.retryAllJob();
        return "Retry Failed Jobs";
    }

    @GetMapping("/retryFailedJob/{instanceId}")
    public String retryFailedJob(@PathVariable long instanceId){
        retryJobConfig.retryJob(instanceId);
        return "Retry Failed Job with id " + instanceId;
    }

    @GetMapping("/triggerCustomerJob")
    public String triggerManualCustomerJob(JobRepository jobRepository,
           PlatformTransactionManager transactionManager,
           ItemReader<CustomerDto> dataCustReader,
           ItemWriter<CustomerDto> csvItemWriterCustomer){

        JobParameters parameters = new JobParametersBuilder()
                .addString("trigger", "manual")
                .addDate("startTime", new Date())
                .toJobParameters();

        try {
            jobLauncher.run(customerConfig.dataCustomerJob(jobRepository,
                    customerConfig.dataCustomerStep(jobRepository, transactionManager, dataCustReader, csvItemWriterCustomer)
                    ), parameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Success Trigger Customer Job";
    }

    @GetMapping("/triggerTransactionJob")
    public String triggerManualTransactionJob(JobRepository jobRepository,
           PlatformTransactionManager transactionManager,
           ItemReader<CustomerTransactionDto> dataTxnReader,
           ItemWriter<CustomerTransactionDto> csvItemWriter){

        JobParameters parameters = new JobParametersBuilder()
                .addString("trigger", "manual")
                .addDate("startTime", new Date())
                .toJobParameters();

        try {
            jobLauncher.run(transactionConfig.dataTransactionJob(jobRepository,
                    transactionConfig.dataTransactionStep(jobRepository, transactionManager, dataTxnReader, csvItemWriter)
            ), parameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Success Trigger Transaction Job";
    }
}