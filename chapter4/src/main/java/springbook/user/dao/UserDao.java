package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.User;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    /*
     * 익명 클래스로 만들면 Junit 인식하지만, 람다로 만들면 예외가 발생한다.
     * */
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getString("id"), rs.getString("name"),
                rs.getString("password"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        final String sql = "INSERT INTO USERS (ID, NAME, PASSWORD) VALUES (?,?,?) ";
        this.jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        final String sql = "SELECT * FROM USERS WHERE ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, this.userRowMapper);
    }

    public List<User> getAll() {
        final String sql = "SELECT * FROM USERS ORDER BY ID";
        return this.jdbcTemplate.query(sql, this.userRowMapper);
    }

    public void deleteAll() {
        final String sql = "delete from USERS;";
        this.jdbcTemplate.update(sql);
    }

    public int getCount() {
        final String sql = "SELECT COUNT(*) FROM USERS";
        return this.jdbcTemplate.queryForInt(sql);
    }

}
