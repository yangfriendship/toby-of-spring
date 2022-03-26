package springbook.user.dao.connectionmaker;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    int count = 0;
    private final ConnectionMaker connectionMaker;

    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    @Override
    public Connection makeConnections() throws ClassNotFoundException, SQLException {
        this.count++;
        return this.connectionMaker.makeConnections();
    }

    public int getCount() {
        return count;
    }
}
