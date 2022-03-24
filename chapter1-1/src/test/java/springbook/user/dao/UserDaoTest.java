package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

public class UserDaoTest {

    // Test target
    UserDao userDao;
    // Fixture
    User user;

    @BeforeEach
    public void setup() {
        this.userDao = new DaoFactory().userDao();
        this.user = new User();
        this.user.setId("id");
        this.user.setName("name");
        this.user.setPassword("password");
    }

    @Test
    void userDaoTest() throws ClassNotFoundException, SQLException {
        userDao.add(this.user);

        User result = userDao.get(this.user.getId());
        assertNotNull(result);
    }

    @Test
    public void daoFactoryTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        ConnectionMaker connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
        assertNotNull(userDao);
        assertNotNull(connectionMaker);
    }

    @Test
    public void singletonTest() {
        DaoFactory daoFactory = new DaoFactory();
        UserDao userDao1 = daoFactory.userDao();
        UserDao userDao2 = daoFactory.userDao();
        assertNotSame(userDao1, userDao2);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao3 = context.getBean("userDao", UserDao.class);
        UserDao userDao4 = context.getBean("userDao", UserDao.class);
        assertSame(userDao3, userDao4);
    }

}