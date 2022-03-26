package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    // Test target
    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao userDao;
    // Fixture
    User user;
    int userSeq = 1;

    @Before
    public void setup() {
        this.user = new User("id" + userSeq++, "name", "password");
    }

    private void insertDummyUser() throws SQLException, ClassNotFoundException {
        this.user.setId("id" + userSeq++);
        this.userDao.add(this.user);
    }

    @Test
    public void userDaoTest() throws ClassNotFoundException, SQLException {
        userDao.deleteAll();
        userDao.add(this.user);
        User result = userDao.get(this.user.getId());
        assertNotNull(result);
    }

    @Test
    public void daoFactoryTest() {
        UserDao userDao = this.context.getBean("userDao", UserDao.class);
        ConnectionMaker connectionMaker = this.context.getBean("connectionMaker",
            ConnectionMaker.class);
        assertNotNull(userDao);
        assertNotNull(connectionMaker);
    }

    @Test
    public void addAndGetTest() throws Exception {
        this.userDao.deleteAll();
        assertEquals(0, this.userDao.getCount());

        this.userDao.add(this.user);
        assertEquals(1, this.userDao.getCount());
        User find = this.userDao.get(this.user.getId());

        assertEqualUser(this.user, find);
    }

    @Test
    public void getUserFailure() throws Exception {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            User find = this.userDao.get(this.user.getId());
        });
    }

    private void assertEqualUser(User user, User find) {
        assertEquals(user.getId(), find.getId());
        assertEquals(user.getName(), find.getName());
        assertEquals(user.getPassword(), find.getPassword());
    }

    @Test
    public void singletonTest() {
        DaoFactory daoFactory = new DaoFactory();
        UserDao userDao1 = daoFactory.userDao();
        UserDao userDao2 = daoFactory.userDao();
        assertNotSame(userDao1, userDao2);

        UserDao userDao3 = this.context.getBean("userDao", UserDao.class);
        UserDao userDao4 = this.context.getBean("userDao", UserDao.class);
        assertSame(userDao3, userDao4);
    }

    @Test
    public void deleteAllTest() throws SQLException, ClassNotFoundException {
        // given: User * 2
        this.userDao.deleteAll();
        insertDummyUser();
        insertDummyUser();
        int count = this.userDao.getCount();
        assertEquals(2, count);
        // when
        this.userDao.deleteAll();

        int countOfDeleteAll = this.userDao.getCount();
        assertEquals(0, countOfDeleteAll);
    }

    @Test
    public void getCountTest() throws SQLException, ClassNotFoundException {
        // given: User * 2
        this.userDao.deleteAll();
        int count = this.userDao.getCount();
        assertEquals(0, count);

        // when: add 1 user
        insertDummyUser();

        // then: count = 1
        int countOfDeleteAll = this.userDao.getCount();
        assertEquals(1, countOfDeleteAll);
    }

}