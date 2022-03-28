package springbook.user.service;

import java.util.List;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {

    private static Level DEFAULT_LEVEL = Level.BASIC;

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        final List<User> users = userDao.getAll();
        for (final User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
                this.userDao.update(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        if (currentLevel == Level.BASIC) {
            user.setLevel(Level.SILVER);
        } else if (currentLevel == Level.SILVER) {
            user.setLevel(Level.GOLD);
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: {
                return user.getLogin() >= 50;
            }
            case SILVER: {
                return user.getRecommend() >= 30;
            }
            case GOLD: {
                return false;
            }
            default:
                throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }


    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(DEFAULT_LEVEL);
        }
        this.userDao.add(user);
    }
}
