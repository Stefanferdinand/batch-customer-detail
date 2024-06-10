package batch.customer.detail.mapper;

import batch.customer.detail.models.dto.CustomerDto;
import batch.customer.detail.models.dto.CustomerTotalDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustDailyMapper implements RowMapper<CustomerDto> {
    @Override
    public CustomerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustId(rs.getString("cust_id"));
        customerDto.setCustDob(rs.getDate("cust_dob"));
        customerDto.setCustGender(rs.getString("cust_gender"));
        customerDto.setCustLocation(rs.getString("cust_location"));
        customerDto.setCustBalance(rs.getBigDecimal("cust_balance"));
        return customerDto;
    }
}
