package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import springbook.user.dao.statementstrategy.StatementStrategy;

public class JdbcContext {

    private final DataSource dataSource;
    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy)
        throws SQLException {
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
}