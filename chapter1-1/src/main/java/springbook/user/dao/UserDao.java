package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import springbook.user.domain.User;

public class UserDao {

    private static final String URL = "jdbc:mysql://localhost/springbook";
    private static final String USER = "youzheng";
    private static final String PASSWORD = "youzheng123";


    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();

        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ");

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.execute();

        ps.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();

        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM USERS WHERE ID = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        connection.close();

        return user;
    }

}
