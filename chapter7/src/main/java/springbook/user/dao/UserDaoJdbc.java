package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

//@Repository("userDao")
@Repository
public class UserDaoJdbc implements UserDao {

    @Autowired
    private SqlService sqlService;

    private JdbcTemplate jdbcTemplate;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        System.out.println("Setting 작동함");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println(this.jdbcTemplate);
    }


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

    @Override
    public int add(User user) {
        return this.jdbcTemplate.update(this.sqlService.getSql("userAdd"), user.getId(), user.getName(),
            user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[]{id},
            this.userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userRowMapper);
    }

    @Override
    public int deleteAll() {
        String userDeleteAll = this.sqlService.getSql("userDeleteAll");
        return this.jdbcTemplate.update(userDeleteAll);
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForInt(this.sqlService.getSql("userGetCount"));
    }

    @Override
    public int update(User user) {
        return this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
            user.getRecommend(), user.getEmail(), user.getId()
        );
    }

}
