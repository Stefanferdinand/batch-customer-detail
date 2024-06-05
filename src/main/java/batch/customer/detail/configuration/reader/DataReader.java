package batch.customer.detail.configuration.reader;

import batch.customer.detail.mapper.CustDetailsMapper;
import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.repository.SqlRepository;
import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@Component
public class DataReader {
    private final SqlRepository repository;
    private final DataSource dataSource;

    public DataReader(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public ItemReader<CustomerDto> dataCustReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<CustomerDto>()
                .dataSource(dataSource)
                .name("dataCustReader")
                .queryProvider(repository.getSQLCustProvider(dataSource))
                .rowMapper(new CustDetailsMapper())
                .pageSize(100)
                .build();
    }



}
