package springbook.user.dao.templatemethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import springbook.user.dao.UserDao;

public class UserDaoDeleteAll extends UserDao {

//    @Override
    protected PreparedStatement makeStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from USERS;");
        return preparedStatement;
    }
}
