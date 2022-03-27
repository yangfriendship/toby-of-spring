package springbook.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    List<User> users;

    @Before
    public void setup() {
        this.users = Arrays.asList(
            new User("woojung", "woojung1", "p1", Level.BASIC, 49, 0),
            new User("woojung", "woojung1", "p1", Level.BASIC, 50, 0),
            new User("woojung", "woojung1", "p1", Level.SILVER, 60, 29),
            new User("woojung", "woojung1", "p1", Level.SILVER, 60, 30),
            new User("woojung", "woojung1", "p1", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void init() {
        assertNotNull(this.userService);
        assertEquals(5, this.users.size());
    }

    private void checkLevel(String id, Level expected) {
        User user = this.userDao.get(id);
        assertEquals(expected, user.getLevel());
    }

    @Test
    public void updateLevelsTest() {
    }


}