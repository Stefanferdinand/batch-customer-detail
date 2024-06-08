package batch.customer.detail.configuration;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RetryJobConfig {

    private JobRepository jobRepository;
    private JobOperator jobOperator;
    private JobExplorer jobExplorer;

    public RetryJobConfig(JobRepository jobRepository, JobOperator jobOperator, JobExplorer jobExplorer) {
        this.jobRepository = jobRepository;
        this.jobOperator = jobOperator;
        this.jobExplorer = jobExplorer;
    }

    public boolean retryJob(){
        // get all unique job names
        var jobNames = jobExplorer.getJobNames().stream().toList();

        // get all job instances except for retry failed jobs
        List<JobInstance> jobInstances = new ArrayList<>();
        for(int i = 0 ; i < jobNames.size(); i++){
            if(!jobNames.get(i).equals("retryAllFailedJobs")){
                jobInstances.addAll(jobExplorer.findJobInstancesByJobName(jobNames.get(i), 0, 10000));
            }
        }

        // restart all last job executions that have status & exit_code FAILED
        for(int i = 0 ; i < jobInstances.size(); i++){
            var lastJobExec = jobExplorer.getLastJobExecution(jobInstances.get(i));

            if(lastJobExec != null
                    && !lastJobExec.isRunning()
                    && lastJobExec.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())
                    && lastJobExec.getStatus().equals(BatchStatus.FAILED))
            {
                try {
                    jobOperator.restart(lastJobExec.getJobId());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }

        return true;
    }
}
