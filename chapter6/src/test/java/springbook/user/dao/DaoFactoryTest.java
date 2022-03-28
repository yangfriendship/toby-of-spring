package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class DaoFactoryTest {

    @Test
    void dataSourceTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            DaoFactory.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        assertNotNull(dataSource);
    }

}