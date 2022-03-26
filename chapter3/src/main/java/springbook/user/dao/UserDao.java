package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.dao.statementstrategy.StatementStrategy;
import springbook.user.domain.User;

public class UserDao {

    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public UserDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
    }

    class MemberInnerAddStatementStrategy implements StatementStrategy {

        private final User user;

        public MemberInnerAddStatementStrategy(User user) {
            this.user = user;
        }

        @Override
        public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        }
    }

    public void add(User user) throws SQLException {

        class LocalClassAddStatementStrategy implements StatementStrategy {

            @Override
            public PreparedStatement makePreparedStatement(Connection connection)
                throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }

//        StatementStrategy addStatementStrategy = new MemberInnerAddStatementStrategy();
//        StatementStrategy addStatementStrategy = new LocalClassAddStatementStrategy();
        jdbcContext.jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection)
                throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
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
        final String sql = "delete from USERS;";
        this.jdbcContext.executeSql(sql);
    }


    public int getCount() throws SQLException {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            prepareStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM USERS");
            resultSet = prepareStatement.executeQuery();
            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                throw e;
            }
            try {
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
            } catch (SQLException e) {
                throw e;
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
    }

}
