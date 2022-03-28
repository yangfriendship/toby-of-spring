package springbook.user.service;

import javax.sql.DataSource;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.domain.User;

public class TestUserService extends UserService {

    private String id;

    public TestUserService(String id, PlatformTransactionManager transactionManager) {
        this.id = id;
        super.setTransactionManager(transactionManager);
    }


    @Override
    protected void upgradeLevel(User user) {
        if (id.equals(user.getId())) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }

    public static class TestUserServiceException extends RuntimeException {

    }
}
