package springbook.user.sqlservice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ConcurrentHashMapSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @Before
    public void setup() {
        this.sqlRegistry = new ConcurrentHashMapSqlRegistry();
        this.sqlRegistry.registerSql("KEY1", "SQL1");
        this.sqlRegistry.registerSql("KEY2", "SQL2");
        this.sqlRegistry.registerSql("KEY3", "SQL3");
    }

    private void checkFindResult(String expect1, String expect2, String expect3) {
        Assertions.assertEquals(expect1, this.sqlRegistry.findSql("KEY1"));
        Assertions.assertEquals(expect2, this.sqlRegistry.findSql("KEY2"));
        Assertions.assertEquals(expect3, this.sqlRegistry.findSql("KEY3"));
    }

    @Test
    public void findTest() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    @Test
    public void unknownKeyTest() {
        assertThrows(SqlNotFoundException.class, () -> {
            this.sqlRegistry.findSql("KDJFLKD");
        });
    }

    @Test
    public void updateSingle() {
        final String updateSql = "UPDATE_SQL";
        this.sqlRegistry.updateSql("KEY2", updateSql);
        checkFindResult("SQL1", updateSql, "SQL3");
    }

    @Test
    public void updateMulti() {
        final String updateSql1 = "UPDATE_SQL";
        final String updateSql2 = "UPDATE_SQL";
        Map<String, String> multiUpdate = new HashMap<>();
        multiUpdate.put("KEY1", updateSql1);
        multiUpdate.put("KEY2", updateSql2);
        this.sqlRegistry.updateSql(multiUpdate);
        checkFindResult(updateSql1, updateSql2, "SQL3");
    }

    @Test
    public void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> {
            final String updateSql1 = "UPDATE_SQL";
            this.sqlRegistry.updateSql("KyoKyo", updateSql1);
        });
    }

}