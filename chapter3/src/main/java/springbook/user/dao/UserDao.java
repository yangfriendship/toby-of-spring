package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.dao.statementstrategy.StatementStrategy;
import springbook.user.domain.User;

public class UserDao {

    private DataSource dataSource;
    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public UserDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
        String sql = "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ";

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
//        jdbcContext.jdbcContextWithStatementStrategy(connection -> {
//            PreparedStatement ps = connection.prepareStatement(
//                sql);
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getName());
//            ps.setString(3, user.getPassword());
//            return ps;
//        });
        this.jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM USERS WHERE ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getString("id"), rs.getString("name")
                    , rs.getString("password"));
            }
        });
    }

    public List<User> getAll() {
        final String sql = "SELECT * FROM USERS ORDER BY ID";
        return this.jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getString("id"), rs.getString("name")
                    , rs.getString("password"));
            }
        });
    }

    public void deleteAll() throws SQLException {
        final String sql = "delete from USERS;";
//        this.jdbcContext.executeSql(sql);
//        Implement PreparedStatementCreator
//        this.jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                return con.prepareStatement(sql);
//            }
//        });
        this.jdbcTemplate.update(sql);
    }

    public int getCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM USERS";
//       Implement PreparedStatement, ResultSetExtractor
//        return this.jdbcTemplate.query(new PreparedStatementCreator() {
//                                    @Override
//                                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                                        return con.prepareStatement(sql);
//                                    }
//                                },
//            new ResultSetExtractor<Integer>() {
//                @Override
//                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                    rs.next();
//                    return rs.getInt(1);
//                }
//            }
//        );
        return this.jdbcTemplate.queryForInt(sql);
    }

}
