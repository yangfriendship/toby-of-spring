package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

class XmlUserDaoTest {

    @Test
    void xmlTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        ConnectionMaker connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
        assertNotNull(connectionMaker);
        assertNotNull(userDao);
        assertTrue(connectionMaker instanceof SimpleConnectionMaker);
    }

}