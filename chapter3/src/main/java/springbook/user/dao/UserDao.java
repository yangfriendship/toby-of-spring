package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.dao.statementstrategy.DeleteAllStatementStrategy;
import springbook.user.dao.statementstrategy.StatementStrategy;
import springbook.user.domain.User;

public class UserDao {

    private DataSource dataSource;

    public UserDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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
        jdbcContextWithStatementStrategy(new StatementStrategy() {
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
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection)
                throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from USERS;");
                return preparedStatement;
            }
        });
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        Connection connection = null;
        PreparedStatement prepareStatement = null;

        try {
            connection = dataSource.getConnection();
            prepareStatement = strategy.makePreparedStatement(connection);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (prepareStatement != null) {
                try {
                    prepareStatement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }

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
