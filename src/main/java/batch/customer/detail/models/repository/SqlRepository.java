package batch.customer.detail.models.repository;

import lombok.Getter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;

import javax.sql.DataSource;

public class SqlRepository {

    private final String SQL_TXN = "SELECT sum(trans_amount) amount, count(cust_id) total, cust_id FROM transaction WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '?' GROUP BY cust_id";
    public String getSQL_TXN(String date){
        return SQL_TXN.replace("?", date);
    }

    public PagingQueryProvider getSQLCustProvider(DataSource dataSource) throws Exception {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setSelectClause(
                "SELECT c.cust_id, c.cust_dob, c.cust_gender," +
                        " c.cust_location, c.cust_balance, sum(t.trans_amount) expenses"
        );
        factoryBean.setFromClause(
                " FROM customer_details c" +
                        " LEFT JOIN transaction t ON c.cust_id = t.cust_id"
        );
        factoryBean.setGroupClause("GROUP BY c.cust_id");
        factoryBean.setSortKey("c.cust_id");
        return factoryBean.getObject();
    }
}
