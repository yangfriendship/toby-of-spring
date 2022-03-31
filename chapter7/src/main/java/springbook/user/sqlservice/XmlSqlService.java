package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.util.StringUtils;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        try {
            String contextPath = Sqlmap.class.getPackage().getName();
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                this.getClass().getResourceAsStream("/sqlmap.xml"));

            for (SqlType sqlType : sqlmap.getSql()) {
                this.sqlMap.put(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException();
        }
    }

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
