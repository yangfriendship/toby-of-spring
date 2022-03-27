package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.connectionmaker.ConnectionMaker;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    // Test target
    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;
    // Fixture
    User user;
    List<User> users;
    int userSeq = 1;

    @Before
    public void setup() {
        this.user = new User("id" + userSeq++, "name", "password");
        this.users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User("id" + userSeq++, UUID.randomUUID().toString().substring(0, 5),
                "password");
            users.add(user);
        }
    }

    private void insertDummyUser() {
        this.user.setId("id" + userSeq++);
        this.userDao.add(this.user);
    }

    @Test
    public void userDaoTest() {
        userDao.deleteAll();
        userDao.add(this.user);
        User result = userDao.get(this.user.getId());
        assertNotNull(result);
    }

    @Test
    public void daoFactoryTest() {
        UserDao userDaoJdbc = this.context.getBean("userDaoJdbc", UserDao.class);
        ConnectionMaker connectionMaker = this.context.getBean("connectionMaker",
            ConnectionMaker.class);
        assertNotNull(userDaoJdbc);
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
        UserDao userDaoJdbc1 = daoFactory.userDao();
        UserDao userDaoJdbc2 = daoFactory.userDao();
        assertNotSame(userDaoJdbc1, userDaoJdbc2);

        UserDao userDaoJdbc3 = this.context.getBean("userDaoJdbc", UserDao.class);
        UserDao userDaoJdbc4 = this.context.getBean("userDaoJdbc", UserDao.class);
        assertSame(userDaoJdbc3, userDaoJdbc4);
    }

    @Test
    public void deleteAllTest() {
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
    public void getCountTest() {
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

    @Test
    public void getAllTest() throws Exception {
        this.userDao.deleteAll();

        this.userDao.add(this.users.get(0));
        final List<User> all1 = this.userDao.getAll();
        assertEquals(1, all1.size());

        this.userDao.add(this.users.get(1));
        final List<User> all2 = this.userDao.getAll();
        assertEquals(2, all2.size());

        this.userDao.add(this.users.get(2));
        final List<User> getAllResult = this.userDao.getAll();
        assertEquals(3, getAllResult.size());
        this.users.sort(Comparator.comparing(User::getId));

        for (int i = 0; i < getAllResult.size(); i++) {
            assertEqualUser(getAllResult.get(i), this.users.get(i));
        }
    }

    @Test
    public void getAllNoResult() throws Exception {
        this.userDao.deleteAll();
        final List<User> result = this.userDao.getAll();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void duplicateKeyTest() {
        this.userDao.deleteAll();
        final User user = this.users.get(0);
        final User user2 = this.users.get(0);

        DataAccessException exception = assertThrows(DuplicateKeyException.class, () -> {
            this.userDao.add(user);
            this.userDao.add(user2);
        });

        System.out.println("");
    }

    @Test
    public void sqlExceptionTranslateTest() {
        this.userDao.deleteAll();
        final User user = this.users.get(0);
        final User user2 = this.users.get(0);

        this.userDao.add(user);
        try {
            this.userDao.add(user2);
        } catch (DuplicateKeyException e) {
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(
                this.dataSource);

            DataAccessException translate = translator.translate(null, null,
                (SQLException) e.getRootCause());

            assertTrue(translate instanceof DuplicateKeyException);
        }
    }


}