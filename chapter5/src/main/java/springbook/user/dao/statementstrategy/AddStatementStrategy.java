package springbook.user.dao.statementstrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import springbook.user.domain.User;

public class AddStatementStrategy implements StatementStrategy {

    private final User user;

    public AddStatementStrategy(User user) {
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
