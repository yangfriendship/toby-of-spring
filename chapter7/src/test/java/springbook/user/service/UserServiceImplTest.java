package springbook.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.AppContext;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.TestUserServiceImpl.TestUserServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppContext.class})
public class UserServiceImplTest {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
    @Autowired
    ApplicationContext ctx;
    @Autowired
    UserService testUserService;
    @Resource
    EmbeddedDatabase embeddedDatabase;
    List<User> users;

    @Before
    public void setup() {
        this.users = Arrays.asList(
            new User("woojung0", "woojung0", "p0", "email0", Level.BASIC,
                MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("woojung1", "woojung1", "p1", "email1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,
                0),
            new User("woojung2", "woojung2", "p2", "email2", Level.SILVER, 60,
                MIN_RECOMMEND_FOR_GOLD - 1),
            new User("woojung3", "woojung3", "p3", "email3", Level.SILVER, 60,
                MIN_RECOMMEND_FOR_GOLD),
            new User("woojung4", "woojung4", "p4", "email4", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void init() {
        this.userDao.deleteAll();

        assertNotNull(this.userService);
        assertEquals(5, this.users.size());
    }

    private void checkLevel(User user, boolean expected) {
        User updatedUser = this.userDao.get(user.getId());
        assertEquals(expected, user.getLevel() != updatedUser.getLevel());
    }

    public void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }


    @Test
    @DirtiesContext
    public void updateLevelsTest() {
        this.userDao.deleteAll();
        for (User user : this.users) {
            this.userDao.add(user);
        }
        this.userService.upgradeLevels();
    }

    @Test
    public void mockUpgradeLevel() {

        // given
        UserDao mockUserDao = mock(UserDao.class);
        MailSender mailSender = mock(MailSender.class);
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(mockUserDao);
        userService.setMailSender(mailSender);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(mockUserDao.getAll()).thenReturn(this.users);

        //when
        userService.upgradeLevels();

        // then
        verify(mockUserDao, times(2)).update(captor.capture());
        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        List<User> values = captor.getAllValues();
        assertEquals(values.get(0).getLevel(), Level.SILVER);
        assertEquals(values.get(0).getEmail(), this.users.get(1).getEmail());
        assertEquals(values.get(1).getEmail(), this.users.get(3).getEmail());
    }


    @Test
    public void addTest() {
        this.userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        this.userService.add(userWithLevel);
        this.userService.add(userWithoutLevel);

        User userWithLevelRead = this.userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = this.userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());

    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() {
        this.userService.deleteAll();

//        testUserService.setUserDao(this.userDao);
//        testUserService.setMailSender(this.mailSender);

//        ProxyFactoryBean factoryBean = this.ctx.getBean("&userService", ProxyFactoryBean.class);
//        factoryBean.setTarget(testUserService);
//        UserService txUserService = (UserService) factoryBean.getObject();
        for (User user : this.users) {
            this.userDao.add(user);
        }
        try {
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException Expected!");
        } catch (TestUserServiceException e) {
            // do nothing...
        }
        checkLevel(users.get(0), false);
        checkLevel(users.get(1), false);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), false);
        checkLevel(users.get(4), false);
    }

    @Test
    public void transactionSync() {
        this.userService.deleteAll();
        this.userService.add(this.users.get(0));
        int count = this.userService.getCount();
        assertEquals(1, count);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus transaction = this.transactionManager.getTransaction(definition);
        this.userService.deleteAll();

        this.userService.add(this.users.get(1));
        this.userService.add(this.users.get(2));

        this.transactionManager.rollback(transaction);

        assertEquals(1, count);
        transaction.flush();
    }


    static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            this.requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage[] simpleMessages) throws MailException {

        }
    }

    static class MockUserDao implements UserDao {

        private List<User> users;
        private List<User> upgraded = new ArrayList<>();

        public List<User> getUpgraded() {
            return upgraded;
        }

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        @Override
        public int add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            return this.users.stream()
                .filter(user -> user.getName().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public int deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int update(User user) {
            this.upgraded.add(user);
            return 0;
        }
    }

}