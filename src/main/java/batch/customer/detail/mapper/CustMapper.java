package batch.customer.detail.mapper;

import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustMapper implements RowMapper<CustomerTransactionDto> {
    @Override
    public CustomerTransactionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerTransactionDto customerTransactionDto = new CustomerTransactionDto();
        customerTransactionDto.setAmount(rs.getBigDecimal("amount"));
        customerTransactionDto.setCustId(rs.getString("cust_id"));
        customerTransactionDto.setTotal(rs.getLong("total"));
        return customerTransactionDto;
    }
}
