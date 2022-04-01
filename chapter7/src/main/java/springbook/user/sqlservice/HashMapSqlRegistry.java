package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public class HashMapSqlRegistry implements SqlRegistry {

    private final Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalException {
        final String sql = this.sqlMap.get(key);
        if (!StringUtils.hasText(sql)) {
            throw new SqlRetrievalException("");
        }
        return sql;
    }
}
