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
            new User("woojung0", "woojung0", "p0", Level.BASIC, 49, 0),
            new User("woojung1", "woojung1", "p1", Level.BASIC, 50, 0),
            new User("woojung2", "woojung2", "p2", Level.SILVER, 60, 29),
            new User("woojung3", "woojung3", "p3", Level.SILVER, 60, 30),
            new User("woojung4", "woojung4", "p4", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void init() {
        assertNotNull(this.userService);
        assertEquals(5, this.users.size());
    }

    private void checkLevel(User user, Level expected) {
        User findUser = this.userDao.get(user.getId());
        assertEquals(expected, findUser.getLevel());
    }

    @Test
    public void updateLevelsTest() {
        this.userDao.deleteAll();
        for (final User user : this.users) {
            this.userDao.add(user);
        }

        userService.upgradeLevels();
        checkLevel(this.users.get(0), Level.BASIC);
        checkLevel(this.users.get(1), Level.SILVER);
        checkLevel(this.users.get(2), Level.SILVER);
        checkLevel(this.users.get(4), Level.GOLD);
        checkLevel(this.users.get(3), Level.GOLD);
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


}