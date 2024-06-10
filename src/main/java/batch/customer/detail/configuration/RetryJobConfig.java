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

    public boolean retryAllJob(){
        // get all unique job names
        var jobNames = jobExplorer.getJobNames().stream().toList();

        // get all job instances except for retry failed jobs
        List<JobInstance> jobInstances = new ArrayList<>();
        for(int i = 0 ; i < jobNames.size(); i++){
            if(!jobNames.get(i).equals("retryAllFailedJobs")){
                jobInstances.addAll(jobExplorer.findJobInstancesByJobName(jobNames.get(i), 0, 10000));
            }
        }

        // restart all last job executions that have status & exit_code FAILED || STOPPED
        for(int i = 0 ; i < jobInstances.size(); i++){
            retryJobLogic(jobInstances.get(i));
        }

        return true;
    }

    public boolean retryJob(long instanceId){
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        System.out.println("Getting Job Instance: " + jobInstance.getInstanceId());
        if(jobInstance != null){
            return retryJobLogic(jobInstance);
        }

        return true;
    }

    public boolean retryJobLogic(JobInstance jobInstance){
        var lastJobExec = jobExplorer.getLastJobExecution(jobInstance);
        System.out.println("Retrying Last Job Execution: " + lastJobExec.getJobInstance().getInstanceId());

        if(lastJobExec != null
                && !lastJobExec.isRunning()
                && (
                lastJobExec.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())
                || lastJobExec.getExitStatus().getExitCode().equals(ExitStatus.STOPPED.getExitCode())
                )
                &&(
                lastJobExec.getStatus().equals(BatchStatus.FAILED)
                || lastJobExec.getStatus().equals(BatchStatus.STOPPED)
                )){
            try {
                System.out.println("Retrying job with job name: " + lastJobExec.getJobInstance().getJobName());
                jobOperator.restart(lastJobExec.getJobInstance().getInstanceId());
            } catch (Exception e){
                System.out.println(e.getMessage());
                 throw new RuntimeException(e.getMessage());
            }
        }else{
            return false;
        }

        return true;
    }
}
