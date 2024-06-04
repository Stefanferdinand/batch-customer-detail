package batch.customer.detail.models.repository;

public class SqlRepository {

    private final String SQL_TXN = "SELECT sum(trans_amount) amount, count(cust_id) total, cust_id FROM transaction WHERE TO_CHAR(trans_date, 'dd-mm-yy') = '?' GROUP BY cust_id";
    public String getSQL_TXN(String date){
        return SQL_TXN.replace("?", date);
    }

}
