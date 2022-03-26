package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.dao.connectionmaker.ConnectionMaker;
import springbook.user.dao.connectionmaker.SimpleConnectionMaker;

class XmlUserDaoJdbcTest {

    @Test
    void xmlTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "applicationContext.xml");
        UserDao userDaoJdbc = context.getBean("userDaoJdbc", UserDao.class);
        ConnectionMaker connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
        assertNotNull(connectionMaker);
        assertNotNull(userDaoJdbc);
        assertTrue(connectionMaker instanceof SimpleConnectionMaker);
    }

}