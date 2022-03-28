package springbook.user.service;

import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.Setter;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {

    private static Level DEFAULT_LEVEL = Level.BASIC;

    @Setter
    private UserDao userDao;
    @Setter
    private DataSource dataSource;

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        assert this.dataSource != null;
        Connection connection = DataSourceUtils.getConnection(this.dataSource);
        connection.setAutoCommit(false);
        try {
            final List<User> users = userDao.getAll();
            for (final User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }

    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);

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
