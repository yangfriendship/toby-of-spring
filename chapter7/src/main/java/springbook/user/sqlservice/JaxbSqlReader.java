package springbook.user.sqlservice;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.Setter;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class JaxbSqlReader implements SqlReader {

    private static final String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";

    @Setter
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

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
