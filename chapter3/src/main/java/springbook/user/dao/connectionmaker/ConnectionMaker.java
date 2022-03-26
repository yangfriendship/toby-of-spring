package springbook.user.dao.connectionmaker;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

    Connection makeConnections() throws ClassNotFoundException, SQLException;
}
