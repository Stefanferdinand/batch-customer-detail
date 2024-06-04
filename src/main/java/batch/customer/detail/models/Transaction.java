package batch.customer.detail.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "trans_id")
    private String transId;
    @Column(name = "trans_date")
    private LocalDateTime transDate;
    @Column(name = "trans_amount")
    private BigDecimal transAmount;
    @Column(name = "cust_id")
    private String custId;
}
