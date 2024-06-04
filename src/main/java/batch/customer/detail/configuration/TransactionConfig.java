

import batch.customer.detail.models.Transaction;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class TransactionConfig {

    @Bean
    public Job dataTransactionJob(
            JobRepository jobRepository,
            Step dataTransactionStep
    ){
        return new JobBuilder("dataTransactionJob", jobRepository)
                .start(dataTransactionStep)
                .build();
    }

    @Bean
    public Step dataTransactionStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<CustomerTransactionDto> dataTxnReader,
            ItemWriter<CustomerTransactionDto> csvItemWriter
    ){
        return new StepBuilder("dataTransactionStep", jobRepository)
                .<CustomerTransactionDto, CustomerTransactionDto>chunk(1000, transactionManager)
                .reader(dataTxnReader)
                .writer(csvItemWriter)
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
