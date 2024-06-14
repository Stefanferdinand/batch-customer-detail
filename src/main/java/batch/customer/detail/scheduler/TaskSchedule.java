package batch.customer.detail.scheduler;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.utils.DefaultJobParam;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TaskSchedule {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private AppConstant appConstant;

//    @Scheduled(cron = "*/10 * * * * *") // run every 10 seconds
    @Scheduled(cron = "0 5 0 * * *") // run every day at 00:05 am
    public void transSchedule(){
        try{
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(appConstant.getTransactionJobName()), new DefaultJobParam().init);
            System.out.println(jobExecution.getExitStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 5 0 L * ?") //last day of month at 00:05 am
    public void customerTotalSchedule(){
        try {
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(appConstant.getCustomerJobName()), new DefaultJobParam().init);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
