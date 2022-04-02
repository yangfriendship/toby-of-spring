package springbook.user.sqlservice;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.StringUtils;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private Map<String, String> sqlmap = new ConcurrentHashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlmap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalException {
        final String sql = this.sqlmap.get(key);
        if (!StringUtils.hasText(sql)) {
            throw new SqlNotFoundException(key + "는 존재하지 않는 SQL 입니다.");
        }
        return sql;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        ifNotExistKeyThrows(key);
        this.sqlmap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        final Set<Entry<String, String>> entries = sqlmap.entrySet();
        for (Entry<String, String> entry : entries) {
            ifNotExistKeyThrows(entry.getKey());
            this.sqlmap.put(entry.getKey(), entry.getValue());
        }
    }

    private void ifNotExistKeyThrows(String key) {
        if (!this.sqlmap.containsKey(key)) {
            throw new SqlUpdateFailureException(key + "에 해당하는 Sql 이 존재하지 않습니다.");
        }
    }
}
