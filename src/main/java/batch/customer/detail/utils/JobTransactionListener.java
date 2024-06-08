package batch.customer.detail.utils;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JobTransactionListener implements JobExecutionListener {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public JobTransactionListener(JobLauncher jobLauncher, JobRegistry jobRegistry){
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        System.out.println(jobExecution.getExitStatus().getExitCode());
        if (jobExecution.getExitStatus().getExitCode().equalsIgnoreCase("COMPLETED")) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("odate", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")))
//                    .addLong("testing", System.currentTimeMillis()) //biar bisa run berkali2
                    .toJobParameters();
            try{
//                jobLauncher.run(dataCustomerJob, jobParameters);
                jobLauncher.run(jobRegistry.getJob("dataCustomerJob"), jobParameters);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}