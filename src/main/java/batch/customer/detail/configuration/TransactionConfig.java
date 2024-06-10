package batch.customer.detail.configuration;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import batch.customer.detail.utils.JobTransactionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
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
public class TransactionConfig {
    @Autowired
    private AppConstant appConstant;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    public Job dataTransactionJob(
            JobRepository jobRepository,
            Step dataTransactionStep
    ){
        return new JobBuilder(appConstant.getTransactionJobName(), jobRepository)
                .start(dataTransactionStep)
                .listener(new JobTransactionListener(jobLauncher, jobRegistry, appConstant))
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
                .<CustomerTransactionDto, CustomerTransactionDto>chunk(appConstant.getChunk(), transactionManager)
                .reader(dataTxnReader)
                .writer(csvItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    //harusnya ini auto, cuma terkadang gak auto ke register
//    @Autowired
//    public JobRegistry jobRegistry;
//    @Bean
//    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
//        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
//        postProcessor.setJobRegistry(jobRegistry);
//        return postProcessor;
//    }

}
