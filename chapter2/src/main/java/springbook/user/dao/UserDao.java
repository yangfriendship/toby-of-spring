package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.metadata.SqlServerCallMetaDataProvider;
import springbook.user.domain.User;

public class UserDao {

    //    private ConnectionMaker connectionMaker;
    private DataSource dataSource;

    public UserDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = dataSource.getConnection();

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
        Connection connection = dataSource.getConnection();

        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM USERS WHERE ID = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        connection.close();

        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException {
        Connection connection = dataSource.getConnection();

        PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM USERS");
        prepareStatement.executeUpdate();

        prepareStatement.close();
        connection.close();
    }

    public int getCount() throws SQLException {
        Connection connection = dataSource.getConnection();

        PreparedStatement prepareStatement = connection.prepareStatement(
            "SELECT COUNT(*) FROM USERS");
        ResultSet resultSet = prepareStatement.executeQuery();
        resultSet.next();

        int count = resultSet.getInt(1);

        resultSet.close();
        prepareStatement.close();
        connection.close();

        return count;
    }

}
