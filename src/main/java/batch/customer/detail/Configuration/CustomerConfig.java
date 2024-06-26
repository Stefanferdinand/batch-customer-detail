package batch.customer.detail.configuration;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.models.dto.CustomerTotalDto;
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
public class CustomerConfig {

    @Autowired
    private AppConstant appConstant;

    @Bean
    public Job dataCustomerJob(
            JobRepository jobRepository,
            Step dataCustomerStep
    ){
        return new JobBuilder(appConstant.getCustomerJobName(), jobRepository)
                .start(dataCustomerStep)
                .build();
    }

    @Bean
    public Step dataCustomerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<CustomerTotalDto> dataCustReader,
            ItemWriter<CustomerTotalDto> csvItemWriterCustomer
    ){
        return new StepBuilder("dataCustomerStep", jobRepository)
                .<CustomerTotalDto, CustomerTotalDto>chunk(appConstant.getChunk(), transactionManager)
                .reader(dataCustReader)
                .writer(csvItemWriterCustomer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

}
