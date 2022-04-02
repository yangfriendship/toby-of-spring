package springbook.learningtest.spring.oxm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.sqlservice.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/OxmTest-context.xml")
public class OxmTest {

    @Autowired
    Unmarshaller unmarshaller;

    @Test
    public void test() throws IOException {
        StreamSource source = new StreamSource(getClass().getResourceAsStream("/sqlmap-test.xml"));
        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);
        assertEquals(3, sqlmap.getSql().size());
        assertEquals(sqlmap.getSql().get(0).getKey(), "add");
        assertEquals(sqlmap.getSql().get(1).getKey(), "get");
        assertEquals(sqlmap.getSql().get(2).getKey(), "delete");

    }

}
