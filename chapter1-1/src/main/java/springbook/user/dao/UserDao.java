package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import springbook.user.domain.User;

public class UserDao extends AbstractUserDao {

    private static final String URL = "jdbc:mysql://localhost/springbook";
    private static final String USER = "youzheng";
    private static final String PASSWORD = "youzheng123";


    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
