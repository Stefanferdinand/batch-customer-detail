package batch.customer.detail.scheduler;

import batch.customer.detail.constant.AppConstant;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TaskSchedule {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private AppConstant appConstant;

//    @Scheduled(cron = "*/10 * * * * *") //setiap 10s pake test
    @Scheduled(cron = "0 5 0 * * *") //setiap jam 00:05
    public void transSchedule(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("odate", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")))
//                .addLong("testing", System.currentTimeMillis()) //biar bisa run berkali2
                .toJobParameters();
        try{
//            jobLauncher.run(shipJob, jobParameters);
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(appConstant.getTransactionJobName()), jobParameters);
            System.out.println(jobExecution.getExitStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
