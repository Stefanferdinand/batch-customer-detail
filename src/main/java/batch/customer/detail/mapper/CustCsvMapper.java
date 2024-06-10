package batch.customer.detail.mapper;

import batch.customer.detail.models.dto.CustomerTransactionDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustCsvMapper implements FieldSetMapper<CustomerTransactionDto> {
    @Override
    public CustomerTransactionDto mapFieldSet(FieldSet fieldSet) throws BindException {
        CustomerTransactionDto customerTransactionDto = new CustomerTransactionDto();
        customerTransactionDto.setCustId(fieldSet.readString("cust_id"));
        customerTransactionDto.setAmount(fieldSet.readBigDecimal("amount"));
        customerTransactionDto.setTotal(fieldSet.readLong("total"));
        return customerTransactionDto;
    }
}
