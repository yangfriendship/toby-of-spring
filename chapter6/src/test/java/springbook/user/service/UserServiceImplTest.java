package springbook.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.TestUserService.TestUserServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceImplTest {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    @Autowired
    UserService userService;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    UserDao userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
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
        assertNotNull(this.userServiceImpl);
        assertEquals(5, this.users.size());
    }

    private void checkLevel(User user, boolean expected) {
        User updatedUser = this.userDao.get(user.getId());
        assertEquals(expected, user.getLevel() != updatedUser.getLevel());
    }

    @Test
    @DirtiesContext
    public void updateLevelsTest() {
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        this.userDao.deleteAll();
        for (final User user : this.users) {
            this.userDao.add(user);
        }

        userServiceImpl.upgradeLevels();

        checkLevel(this.users.get(0), false);
        checkLevel(this.users.get(1), true);
        checkLevel(this.users.get(2), false);
        checkLevel(this.users.get(3), true);
        checkLevel(this.users.get(4), false);

        List<String> requests = mockMailSender.requests;

        assertEquals(2, requests.size());
        assertEquals(this.users.get(1).getEmail(), requests.get(0));
        assertEquals(this.users.get(3).getEmail(), requests.get(1));
    }

    @Test
    public void addTest() {
        this.userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        this.userServiceImpl.add(userWithLevel);
        this.userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = this.userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = this.userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());

    }

    @Test
    public void upgradeAllOrNothing() {
        userDao.deleteAll();

        TestUserService userService = new TestUserService(users.get(3).getId(),
            this.transactionManager);
        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setUserService(userService);
        userServiceTx.setTransactionManager(this.transactionManager);

        userService.setUserDao(this.userDao);
        userService.setMailSender(this.mailSender);
        for (User user : this.users) {
            this.userDao.add(user);
        }
        try {
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException Expected!");
        } catch (TestUserServiceException e) {
            // do nothing...
        }
        checkLevel(users.get(1), false);
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

}