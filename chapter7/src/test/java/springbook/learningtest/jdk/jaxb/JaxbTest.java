package springbook.learningtest.jdk.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class JaxbTest {

    @Test
    public void readSqlmapTest() throws Exception {

        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
            getClass().getResourceAsStream("/sqlmap.xml"));

        System.out.println("");
    }

}