package batch.customer.detail.utils;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

public class DefaultJobParam {

    public JobParameters init;
    public JobParameters test;

    public DefaultJobParam(){
        init = new JobParametersBuilder()
                .addLocalDate("date", LocalDate.now())
                .toJobParameters();

        test = new JobParametersBuilder()
                .addLocalDate("date", LocalDate.now())
                .addLocalTime("time", LocalTime.now()) //just for testing
                .toJobParameters();
    }
}
