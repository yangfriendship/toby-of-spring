package springbook.user.service;

import java.util.List;
import springbook.user.domain.User;

public class TestUserServiceImpl extends UserServiceImpl {

    private String id;

    public TestUserServiceImpl() {
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<User> getAll() {
        final List<User> users = super.getAll();
        for (User user : users) {
            super.update(user);
        }
        return users;
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
