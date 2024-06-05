package batch.customer.detail.configuration;

import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CustomerConfig {
    @Bean
    public Job dataCustomerJob(
            JobRepository jobRepository,
            Step dataCustomerStep
    ){
        return new JobBuilder("dataCustomerJob", jobRepository)
                .start(dataCustomerStep)
                .build();
    }

    @Bean
    public Step dataCustomerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<CustomerDto> dataCustReader,
            ItemWriter<CustomerDto> csvItemWriterCustomer
    ){
        return new StepBuilder("dataCustomerStep", jobRepository)
                .<CustomerDto, CustomerDto>chunk(1000, transactionManager)
                .reader(dataCustReader)
                .writer(csvItemWriterCustomer)
                .allowStartIfComplete(true)
                .build();
    }

    @Autowired
    public JobRegistry jobRegistry;
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
