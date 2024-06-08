package batch.customer.detail.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConstant {
    @Value("${data.chunk}")
    private int chunk;
}
