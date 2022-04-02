package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestApplicationContext.class)
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
        this.user = new User("id" + userSeq++, "name", "password", "email", Level.BASIC, 0, 0);
        this.users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User("id" + userSeq++, UUID.randomUUID().toString().substring(0, 5),
                "password", "email", Level.valueOf(i + 1), 0, 0);
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

    @Test
    public void updateTest() {
        this.userDao.deleteAll();
        this.userDao.add(this.users.get(0));
        this.userDao.add(this.users.get(1));

        User findUser = this.userDao.get(this.users.get(0).getId());
        assertEqualUser(this.users.get(0), findUser);

        findUser.setName("newName");
        findUser.setPassword("newPwd");
        findUser.setLevel(Level.GOLD);
        findUser.setLogin(findUser.getLogin() + 1);
        findUser.setLogin(findUser.getRecommend() + 1);
        int affectedRowCount = this.userDao.update(findUser);
        assertEquals(1, affectedRowCount);

        User updateUser = this.userDao.get(this.users.get(0).getId());
        assertEqualUser(findUser, updateUser);

        User notUpdateUser = this.userDao.get(this.users.get(1).getId());
        assertEqualUser(this.users.get(1), notUpdateUser);

    }

    public void assertEqualUserWithoutId(User user, User find) {
        assertEqualUserWithoutId(user, find, true);
    }

    public void assertEqualUserWithoutId(User user, User find, boolean isExpectEqual) {
        assertEquals(user.getName().equals(find.getName()), isExpectEqual);
        assertEquals(user.getPassword().equals(find.getPassword()), isExpectEqual);
        assertEquals(user.getLevel().equals(find.getLevel()), isExpectEqual);
        assertEquals((user.getLogin() == find.getLogin()), isExpectEqual);
        assertEquals((user.getRecommend() == find.getRecommend()), isExpectEqual);
    }

    private void assertEqualUser(User user, User find) {
        assertEquals(user.getId(), find.getId());
        assertEqualUserWithoutId(user, find, true);
    }

    private void assertEqualUser(User user, User find, boolean isExpectEqual) {
        assertEquals(user.getId().equals(find.getId()), isExpectEqual);
        assertEqualUserWithoutId(user, find, isExpectEqual);
    }

    private <T> void checkSameObject(T obj1, T obj2, TestFunc<T>... predicates) {
        for (TestFunc<T> func : predicates) {
            if (!func.isSame(obj1, obj2)) {
                fail();
            }
        }
    }

    @FunctionalInterface
    interface TestFunc<T> {

        boolean isSame(T obj1, T obj2);
    }

}