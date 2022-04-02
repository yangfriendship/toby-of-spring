package springbook.user.sqlservice;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import lombok.Setter;
import org.springframework.oxm.Unmarshaller;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    private BaseSqlService baseSqlService = new BaseSqlService();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    @Setter
    private static class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private static final String DEFAULT_SQL_MAP_FILE = "/sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQL_MAP_FILE;

        @Override
        public void read(SqlRegistry sqlRegistry) {

            Source source = new StreamSource(getClass().getResourceAsStream(this.sqlmapFile));
            try {
                Sqlmap sqlmap = (Sqlmap) (this.unmarshaller.unmarshal(source));
                for (SqlType sqlType : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다.", e);
            }
        }
    }

    @PostConstruct
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        this.baseSqlService.load();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalException(e);
        }
    }
}
