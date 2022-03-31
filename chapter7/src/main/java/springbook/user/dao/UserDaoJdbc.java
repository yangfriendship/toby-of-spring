package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {

    @Setter
    private String sqlAdd;
    @Setter
    private Map<String, String> sqlMap;
    private JdbcTemplate jdbcTemplate;

    /*
     * 익명 클래스로 만들면 Junit 인식하지만, 람다로 만들면 예외가 발생한다.
     * */
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getString("id"), rs.getString("name"),
                rs.getString("password"), rs.getString("email"), Level.valueOf(rs.getInt("level")),
                rs.getInt("login"), rs.getInt("recommend"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(User user) {
        return this.jdbcTemplate.update(this.sqlMap.get("add"), user.getId(), user.getName(),
            user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), new Object[]{id},
            this.userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlMap.get("getAll"), this.userRowMapper);
    }

    @Override
    public int deleteAll() {
        return this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForInt(this.sqlMap.get("getCount"));
    }

    @Override
    public int update(User user) {
        return this.jdbcTemplate.update(this.sqlMap.get("update"),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
            user.getRecommend(), user.getEmail(), user.getId()
        );
    }

}
