package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.Setter;
import org.springframework.util.StringUtils;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private Map<String, String> sqlMap = new HashMap<>();
    @Setter
    private String sqlmapFile;
    @Setter
    private SqlRegistry sqlRegistry;
    @Setter
    private SqlReader sqlReader;

    public XmlSqlService() {
    }

    @PostConstruct
    private void load() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalException {
        if (!StringUtils.hasText(key)) {
            throw new SqlRetrievalException(key + "는 올바른 key 가 아닙니다.");
        }
        final String sql = this.sqlRegistry.findSql(key);
        if (!StringUtils.hasText(sql)) {
            throw new SqlRetrievalException(key + "에 해단하는 Sql 이 존재하지 않습니다.");
        }
        return sql;
    }

    /* SqlRegister */
    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalException {
        if (!StringUtils.hasText(key)) {
            throw new RuntimeException();
        }
        String sql = this.sqlMap.get(key);
        if (!StringUtils.hasLength(sql)) {
            throw new RuntimeException();
        }
        return sql;
    }

    /* SqlReader*/
    @Override
    public void read(SqlRegistry sqlRegistry) {
        try {
            String contextPath = Sqlmap.class.getPackage().getName();
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                this.getClass().getResourceAsStream(this.sqlmapFile));

            for (SqlType sqlType : sqlmap.getSql()) {
                sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException();
        }
    }
}
