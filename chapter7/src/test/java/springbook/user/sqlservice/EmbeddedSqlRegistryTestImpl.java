package springbook.user.sqlservice;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedSqlRegistryTestImpl extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase database;

    @Override
    protected UpdatableSqlRegistry createSqlRegistry() {
        this.database = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:/sql/schema.sql")
            .build();
        EmbeddedSqlRegistry sqlRegistry = new EmbeddedSqlRegistry();
        sqlRegistry.setDataSource(this.database);
        return sqlRegistry;
    }

    @After
    public void destroy() {
        this.database.shutdown();
    }

    @Test
    public void transactionUpdateTest() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> updateSql = new HashMap<>();
        updateSql.put("KEY1", "Updated");
        updateSql.put("adsfdsf", "Updated");
        try {
            this.sqlRegistry.updateSql(updateSql);
            fail();
        } catch (SqlUpdateFailureException e) {
            checkFindResult("SQL1", "SQL2", "SQL3");
        }
    }
}
