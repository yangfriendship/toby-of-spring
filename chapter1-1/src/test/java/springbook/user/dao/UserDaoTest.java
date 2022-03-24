package springbook.user.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springbook.user.domain.User;

public class UserDaoTest {

    // Test target
    UserDao userDao;
    // Fixture
    User user;

    @BeforeEach
    public void setup() {
        this.userDao = new UserDao();
        this.user = new User();
        this.user.setId("id");
        this.user.setName("name");
        this.user.setPassword("password");
    }

    @Test
    void userDaoTest() throws ClassNotFoundException, SQLException {
        userDao.add(this.user);

        User result = userDao.get(this.user.getId());
        Assertions.assertNotNull(this.user);
    }

}