package batch.customer.detail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchCustomerDetailApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchCustomerDetailApplication.class, args);
	}

}
