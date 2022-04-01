package springbook.user.sqlservice;

import javax.annotation.PostConstruct;
import lombok.Setter;

public class BaseSqlService implements SqlService {

    @Setter
    protected SqlReader sqlReader;
    @Setter
    protected SqlRegistry sqlRegistry;

    @PostConstruct
    public void load() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalException("");
        }
    }

}
