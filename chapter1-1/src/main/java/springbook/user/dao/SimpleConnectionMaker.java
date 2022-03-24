package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    private static final String URL = "jdbc:mysql://localhost/springbook";
    private static final String USER = "youzheng";
    private static final String PASSWORD = "youzheng123";
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public SimpleConnectionMaker() {
    }

    Connection makeNewConnections() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}