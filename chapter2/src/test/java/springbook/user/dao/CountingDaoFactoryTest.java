package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class CountingDaoFactoryTest {

    @Test
    public void countingConnectionMakerTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        ConnectionMaker maker = context.getBean("countingConnectionMaker",
            ConnectionMaker.class);

        assertFalse(!(maker instanceof CountingConnectionMaker));
        Connection connection = maker.makeConnections();
        int count = ((CountingConnectionMaker) maker).getCount();
        assertEquals(1, count);
    }
}