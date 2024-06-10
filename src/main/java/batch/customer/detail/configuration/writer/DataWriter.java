package batch.customer.detail.configuration.writer;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataWriter {
    @Autowired
    private AppConstant appConstant;
    @Bean
    public ItemWriter<CustomerTransactionDto> csvItemWriter(){
        String[] header = new String[]{"custId", "amount", "total"};
        String pathOut = appConstant.getPathOutput() + String.format("summary_%s.csv", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        FlatFileHeaderCallback headerCallback = new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write(String.join(",", header));
            }
        };
        return new FlatFileItemWriterBuilder<CustomerTransactionDto>()
                .name("customerTransactionWriter")
                .resource(new FileSystemResource(pathOut))
                .delimited().delimiter(",")
                .names(header)
                .headerCallback(headerCallback)
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .build();
    }

    @Bean
    public ItemWriter<CustomerDto> csvItemWriterCustomer(){
        String[] header = new String[]{
                "custId", "custDob", "custGender","custLocation", "custBalance", "expenses"
        };
        String pathOut = String.format("data/output/cust_details_%s.csv", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        FlatFileHeaderCallback headerCallback = writer -> writer.write(String.join(",", header));
        return new FlatFileItemWriterBuilder<CustomerDto>()
                .name("customerDetailsWriter")
                .resource(new FileSystemResource(pathOut))
                .delimited().delimiter(",")
                .names(header)
                .headerCallback(headerCallback)
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .build();
    }
}
