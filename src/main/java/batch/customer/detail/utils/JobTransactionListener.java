package batch.customer.detail.utils;

import batch.customer.detail.constant.AppConstant;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;

public class JobTransactionListener implements JobExecutionListener {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final AppConstant appConstant;

    public JobTransactionListener(JobLauncher jobLauncher, JobRegistry jobRegistry, AppConstant appConstant){
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
        this.appConstant = appConstant;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if (jobExecution.getExitStatus().getExitCode().equalsIgnoreCase("COMPLETED")) {
            try{
//                jobLauncher.run(dataCustomerJob, jobParameters);
                jobLauncher.run(jobRegistry.getJob(appConstant.getCustDailyJobName()), new DefaultJobParam().init);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
