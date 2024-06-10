package batch.customer.detail.configuration.reader;

import batch.customer.detail.constant.AppConstant;
import batch.customer.detail.mapper.CustDailyMapper;
import batch.customer.detail.mapper.CustTotalMapper;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTotalDto;
import batch.customer.detail.models.repository.SqlRepository;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataReader {
    private final SqlRepository repository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AppConstant appConstant;

    public DataReader() {
        repository = new SqlRepository();
    }

    @Bean
    public ItemReader<CustomerTransactionDto> dataTxnReader(){
        //pake testing
//        String sql = repository.getSQL_TXN(LocalDate.of(2016, Month.AUGUST, 16).format(DateTimeFormatter.ofPattern("dd-MM-yy")));
        String sql = repository.getSQL_TXN(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
        return new JdbcCursorItemReaderBuilder<CustomerTransactionDto>()
                .dataSource(dataSource)
                .name("dataTxnReader")
                .sql(sql)
                .rowMapper(new BeanPropertyRowMapper<>(CustomerTransactionDto.class))
                .build();
    }

    @Bean
    public ItemReader<CustomerDto> dataCustDailyReader_db() throws Exception {
        String timestamp =  // Yang pertama untuk testing, yang kedua untuk production
//                LocalDate.of(2016, Month.AUGUST, 16).format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
        return new JdbcPagingItemReaderBuilder<CustomerDto>()
                .dataSource(dataSource)
                .name("dataCustDailyReader")
                .queryProvider(repository.getSQLCustDailyProvider(
                        dataSource, timestamp))
                .rowMapper(new CustDailyMapper())
                .pageSize(appConstant.getChunk())
                .build();
    }

//    TODO: Kalau pakai ini, harus tambah satu step lagi. Gak efisien.
//    @Bean
//    public ItemReader<CustomerTransactionDto> dataCustDailyReader_csv() throws Exception {
//        String[] tokens = new String[] {"custId", "amount", "total"};
//        DefaultLineMapper<CustomerTransactionDto> lineMapper = new DefaultLineMapper<>();
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setNames(tokens);
//        lineMapper.setLineTokenizer(tokenizer);
//        lineMapper.setFieldSetMapper(new CustCsvMapper());
//
//        FlatFileItemReader<CustomerTransactionDto> reader = new FlatFileItemReader<>();
//        String pathIn = String.format("data/output/summary_%s.csv", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
//        reader.setResource(new FileSystemResource(pathIn));
//        reader.setLinesToSkip(1);
//        reader.setLineMapper(lineMapper);
//        reader.setSaveState(false);
//        reader.open(new ExecutionContext());
//
//        return reader;
//    }

    @Bean
    public ItemReader<CustomerTotalDto> dataCustReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<CustomerTotalDto>()
                .dataSource(dataSource)
                .name("dataCustReader")
                .queryProvider(repository.getSQLCustProvider(dataSource))
                .rowMapper(new CustTotalMapper())
                .pageSize(appConstant.getChunk())
                .build();
    }
}
