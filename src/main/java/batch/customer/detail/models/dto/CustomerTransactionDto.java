package batch.customer.detail.models.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class CustomerTransactionDto {
    @Column(name = "cust_id")
    private String custId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "total")
    private long total;
}
