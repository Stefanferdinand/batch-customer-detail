package batch.customer.detail.models.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class CustomerTotalDto {
    @Column(name = "cust_id")
    private String custId;
    @Column(name = "cust_dob")
    private Date custDob;
    @Column(name = "cust_gender")
    private String custGender;
    @Column(name = "cust_location")
    private String custLocation;
    @Column(name = "cust_balance")
    private BigDecimal custBalance;
    @Column(name = "expenses")
    private BigDecimal expenses;
}
