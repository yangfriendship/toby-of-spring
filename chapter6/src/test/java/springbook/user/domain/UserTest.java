package springbook.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    User user;

    @Before
    public void setup() {
        user = new User();
    }

    @Test
    public void upgradeLevelTest() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.next() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertEquals(user.getLevel(), level.next());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevelTest() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.next() != null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
        }
    }

}