package batch.customer.detail.models.repository;

import lombok.Getter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;

import javax.sql.DataSource;

public class SqlRepository {

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
        factoryBean.setSortKey("cust_id");
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    public PagingQueryProvider getTransactionSQL(DataSource dataSource, String date) throws Exception {
        SqlPagingQueryProviderFactoryBean bean = new SqlPagingQueryProviderFactoryBean();
        bean.setSelectClause("SELECT sum(trans_amount) amount, count(cust_id) total, cust_id");
        bean.setFromClause("transaction");
        bean.setGroupClause("GROUP BY cust_id");
        bean.setWhereClause(String.format("WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '%s'", date));
        bean.setSortKey("cust_id");
        bean.setDataSource(dataSource);
        return bean.getObject();

    public PagingQueryProvider getSQLCustDailyProvider(
            DataSource dataSource,
            String date) throws Exception {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setSelectClause(
                "SELECT t.cust_id, c.cust_dob, c.cust_gender," +
                        " c.cust_location, c.cust_balance"
        );
        factoryBean.setFromClause(
                "FROM transaction t"
                + " INNER JOIN customer_details c ON t.cust_id = c.cust_id"
        );
        factoryBean.setWhereClause("WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '?'".replace("?", date));
        factoryBean.setSortKey("cust_id");
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }
}
