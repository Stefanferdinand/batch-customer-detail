package batch.customer.detail.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "customer_details")
@ToString
public class CustomerDetails {
    @Id
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
}
