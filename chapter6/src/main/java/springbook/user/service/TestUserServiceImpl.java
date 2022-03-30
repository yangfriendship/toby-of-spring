package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.domain.User;

public class TestUserServiceImpl extends UserServiceImpl {

    private String id;

    public TestUserServiceImpl() {
    }

    public void setId(String id) {
        this.id = id;
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
