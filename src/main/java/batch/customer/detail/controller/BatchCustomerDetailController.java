package batch.customer.detail.controller;

import batch.customer.detail.configuration.CustomerConfig;
import batch.customer.detail.configuration.CustomerDailyConfig;
import batch.customer.detail.configuration.RetryJobConfig;
import batch.customer.detail.configuration.TransactionConfig;
import batch.customer.detail.models.dto.CustomerTotalDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.JobParameters;
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

@RestController
public class BatchCustomerDetailController {

    // configurations
    private final RetryJobConfig retryJobConfig;
    private final CustomerConfig customerConfig;
    private final TransactionConfig transactionConfig;
    private final CustomerDailyConfig customerDailyConfig;

    // reader & writer
    private final ItemReader<CustomerTotalDto> dataCustReader;
    private final ItemWriter<CustomerTotalDto> csvItemWriterCustomer;
    private final ItemReader dataCustDailyReader_db;
    private final ItemWriter csvItemWriterCustomerDaily;
    private ItemReader<CustomerTransactionDto> dataTxnReader;
    private ItemWriter<CustomerTransactionDto> csvItemWriter;

    // job components
    private JobLauncher jobLauncher;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    public BatchCustomerDetailController(RetryJobConfig retryJobConfig,
                                         CustomerConfig customerConfig,
                                         TransactionConfig transactionConfig,
                                         CustomerDailyConfig customerDailyConfig,
                                         ItemReader dataCustReader,
                                         ItemWriter csvItemWriterCustomer,
                                         ItemReader dataCustDailyReader_db,
                                         ItemWriter csvItemWriterCustomerDaily,
                                         ItemReader dataTxnReader,
                                         ItemWriter csvItemWriter,
                                         JobLauncher jobLauncher,
                                         JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager) {
        this.retryJobConfig = retryJobConfig;
        this.customerConfig = customerConfig;
        this.transactionConfig = transactionConfig;
        this.customerDailyConfig = customerDailyConfig;
        this.dataCustReader = dataCustReader;
        this.csvItemWriterCustomer = csvItemWriterCustomer;
        this.dataCustDailyReader_db = dataCustDailyReader_db;
        this.csvItemWriterCustomerDaily = csvItemWriterCustomerDaily;
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

    @GetMapping("/triggerCustomerDailyJob")
    public String triggerManualCustomerDailyJob(){
        try {
            jobLauncher.run(customerDailyConfig.dataCustomerDailyJob(jobRepository,
                    customerDailyConfig.dataCustomerDailyStep(
                            jobRepository, transactionManager,
                            dataCustDailyReader_db, csvItemWriterCustomerDaily
                    )
            ), new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return "Success Trigger Customer Daily Job";
    }
}