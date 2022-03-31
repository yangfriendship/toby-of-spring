package springbook.user.sqlservice;

import java.util.Map;
import lombok.Setter;
import org.springframework.util.StringUtils;

public class SqlServiceImpl implements SqlService {

    @Setter
    private Map<String, String> sqlMap;

    @Override
    public String getSql(String key) throws SqlRetrievalException {
        if (!StringUtils.hasText(key)) {
            throw new SqlRetrievalException(key + "는 올바른 key 가 아닙니다.");
        }
        final String sql = this.sqlMap.get(key);
        if (!StringUtils.hasText(sql)) {
            throw new SqlRetrievalException(key + "에 해단하는 Sql 이 존재하지 않습니다.");
        }
        return sql;
    }
}
