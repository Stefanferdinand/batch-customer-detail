package batch.customer.detail;

import batch.customer.detail.configuration.TransactionConfig;
import batch.customer.detail.constant.AppConstant;
import org.assertj.core.api.FileAssert;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@SpringBootTest
@SpringBatchTest
class BatchCustomerDetailApplicationTests {

	@Autowired
	public JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	public JobRepositoryTestUtils jobRepositoryTestUtils;
	@Autowired
	public AppConstant appConstant;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataTransactionJob")
	private Job transactionJob;

	@AfterEach
	void tearDown() {
		this.jobRepositoryTestUtils.removeJobExecutions();
	}

	private static final String charSet = "utf-8";

	@Test
	void testingTransactionJob() throws Exception {
		File expectedFile = new File(appConstant.getPathOutput() + "expected.csv");
		File resultFile = new File(appConstant.getPathOutput() + String.format("summary_%s", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));

		jobLauncherTestUtils.setJob(transactionJob);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParam());

		Assertions.assertEquals(appConstant.getTransactionJobName(), jobExecution.getJobInstance().getJobName());
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
		if (resultFile.exists()){
			Assertions.assertEquals(Files.contentOf(expectedFile, charSet), Files.contentOf(resultFile, charSet));
		} else {
			Assertions.assertFalse(resultFile.exists());
		}
	}

	@Test
	void testingTransactionStep(){
		File expectedFile = new File(appConstant.getPathOutput() + "expected.csv");
		File resultFile = new File(appConstant.getPathOutput() + String.format("summary_%s", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
//		String sql = String.format("SELECT count(DISTINCT(cust_id)) FROM transaction WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '%s'", LocalDate.of(2016, Month.AUGUST, 16).format(DateTimeFormatter.ofPattern("dd-MM-yy")));
		String sql = String.format("SELECT count(DISTINCT(cust_id)) FROM transaction WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '%s'", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
		Long rowCount = jdbcTemplate.queryForObject(sql, Long.class);

		jobLauncherTestUtils.setJob(transactionJob);
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("dataTransactionStep");

		long readCount = jobExecution.getStepExecutions().stream().toList().get(0).getReadCount();
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
		Assertions.assertEquals(1, jobExecution.getStepExecutions().size());
		Assertions.assertEquals(rowCount, readCount);
	}



	private JobParameters defaultJobParam(){
		return new JobParametersBuilder()
				.addString("odate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
	}

}
