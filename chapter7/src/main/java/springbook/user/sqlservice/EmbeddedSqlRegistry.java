package springbook.user.sqlservice;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class EmbeddedSqlRegistry implements UpdatableSqlRegistry {

    private SimpleJdbcTemplate template;
    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        this.template = new SimpleJdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(
            new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void registerSql(String key, String sql) {
        this.template.update("INSERT INTO SQLMAP VALUES(?,?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalException {
        try {
            return this.template.queryForObject("SELECT SQL_ FROM SQLMAP WHERE KEY_ = ?",
                String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "에 해당하는 Sql 이 존재하지 않습니다.");
        }
    }

    @Override
    public void updateSql(final String key, String sql) throws SqlUpdateFailureException {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                int affected = template.update("UPDATE SQLMAP SET SQL_ = ? WHERE KEY_ = ?",
                    sql, key);
                if (affected == 0) {
                    throw new SqlUpdateFailureException(key + "에 해당하는 Sql 이 존재하지 않습니다.");
                }
            }
        });

    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final Set<Entry<String, String>> entries = sqlmap.entrySet();
                for (Entry<String, String> entry : entries) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
