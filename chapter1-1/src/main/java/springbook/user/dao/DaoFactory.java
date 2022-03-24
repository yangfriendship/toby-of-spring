package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        return new UserDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }

}
