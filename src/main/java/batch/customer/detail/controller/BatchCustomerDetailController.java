package batch.customer.detail.controller;

import batch.customer.detail.configuration.CustomerConfig;
import batch.customer.detail.configuration.RetryJobConfig;
import batch.customer.detail.configuration.TransactionConfig;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
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

    // configurations
    private final RetryJobConfig retryJobConfig;
    private final CustomerConfig customerConfig;
    private final TransactionConfig transactionConfig;

    // reader & writer
    private final ItemReader<CustomerDto> dataCustReader;
    private final ItemWriter<CustomerDto> csvItemWriterCustomer;
    private ItemReader<CustomerTransactionDto> dataTxnReader;
    private ItemWriter<CustomerTransactionDto> csvItemWriter;

    // job components
    private JobLauncher jobLauncher;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;



    public BatchCustomerDetailController(RetryJobConfig retryJobConfig,
                                         CustomerConfig customerConfig,
                                         TransactionConfig transactionConfig,
                                         ItemReader dataCustReader,
                                         ItemWriter csvItemWriterCustomer,
                                         ItemReader dataTxnReader,
                                         ItemWriter csvItemWriter,
                                         JobLauncher jobLauncher,
                                         JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager) {
        this.retryJobConfig = retryJobConfig;
        this.customerConfig = customerConfig;
        this.transactionConfig = transactionConfig;
        this.dataCustReader = dataCustReader;
        this.csvItemWriterCustomer = csvItemWriterCustomer;
        this.dataTxnReader = dataTxnReader;
        this.csvItemWriter = csvItemWriter;
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @GetMapping("/retryAllFailedJob")
    public String retryAllFailedJob(){
        retryJobConfig.retryAllJob();
        return "Retry Failed Jobs";
    }

    @GetMapping("/retryFailedJob/{instanceId}")
    public String retryFailedJob(@PathVariable long instanceId){
        if(retryJobConfig.retryJob(instanceId)){
            return "Retry Failed Job with id " + instanceId;
        }
        return "Unsuccessful Retry Failed Job";
    }

    @GetMapping("/triggerCustomerJob")
    public String triggerManualCustomerJob(){

        try {
            jobLauncher.run(customerConfig.dataCustomerJob(jobRepository,
                    customerConfig.dataCustomerStep(jobRepository, transactionManager, dataCustReader, csvItemWriterCustomer)
                    ), new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Success Trigger Customer Job";
    }

    @GetMapping("/triggerTransactionJob")
    public String triggerManualTransactionJob(){

        try {
            jobLauncher.run(transactionConfig.dataTransactionJob(jobRepository,
                    transactionConfig.dataTransactionStep(jobRepository, transactionManager, dataTxnReader, csvItemWriter)
            ), new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Success Trigger Transaction Job";
    }
}