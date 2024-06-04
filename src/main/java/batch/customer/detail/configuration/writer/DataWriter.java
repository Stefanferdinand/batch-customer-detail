package batch.customer.detail.configuration.writer;

import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataWriter {
    @Bean
    public ItemWriter<CustomerTransactionDto> csvItemWriter(){
        String[] header = new String[]{"custId", "amount", "total"};
        String pathOut = String.format("data/output/summary_%s.csv", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
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
                .build();
    }
}
