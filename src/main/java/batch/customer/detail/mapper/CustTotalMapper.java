package batch.customer.detail.mapper;

import batch.customer.detail.models.dto.CustomerTotalDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustTotalMapper implements RowMapper<CustomerTotalDto> {
    @Override
    public CustomerTotalDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerTotalDto customerTotalDto = new CustomerTotalDto();
        customerTotalDto.setCustId(rs.getString("cust_id"));
        customerTotalDto.setCustDob(rs.getDate("cust_dob"));
        customerTotalDto.setCustGender(rs.getString("cust_gender"));
        customerTotalDto.setCustLocation(rs.getString("cust_location"));
        customerTotalDto.setCustBalance(rs.getBigDecimal("cust_balance"));
        customerTotalDto.setExpenses(rs.getBigDecimal("expenses"));
        return customerTotalDto;
    }
}
