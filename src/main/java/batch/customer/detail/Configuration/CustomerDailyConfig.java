package batch.customer.detail.configuration;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CustomerDailyConfig {

    @Autowired
    private AppConstant appConstant;

    @Bean
    public Job dataCustomerDailyJob(
            JobRepository jobRepository,
            Step dataCustomerDailyStep
    ){
        return new JobBuilder(appConstant.getCustDailyJobName(), jobRepository)
                .start(dataCustomerDailyStep)
                .build();
    }

    @Bean
    public Step dataCustomerDailyStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<CustomerDto> dataCustDailyReader_db,
            ItemWriter<CustomerDto> csvItemWriterCustomerDaily
    ){
        return new StepBuilder("dataCustomerDailyStep", jobRepository)
                .<CustomerDto, CustomerDto>chunk(appConstant.getChunk(), transactionManager)
                .reader(dataCustDailyReader_db)
                .writer(csvItemWriterCustomerDaily)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .allowStartIfComplete(true)
                .build();
    }
}
