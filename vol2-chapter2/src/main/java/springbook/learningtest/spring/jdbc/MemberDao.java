package springbook.learningtest.spring.jdbc;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import springbook.learningtest.spring.jdbc.entity.Member;

public class MemberDao {

    public SimpleJdbcTemplate template;

    public void setDataSource(DataSource dataSource) {
        this.template = new SimpleJdbcTemplate(dataSource);
    }

    /*
     * Sql Parameter
     * */
    public int insertSqlParameter(int id, String name, double point) {
        return this.template.update("insert into member(id,name, point) values(?,?,?)", id, name,
            point);
    }

    /*
     * Placeholder
     * Map 을 전달할 수 있다.
     * */
    public int insertPlaceholder(int id, String name, double point) {
        return this.template.update("insert into member(id,name, point) values(:id,:name,:point)");
    }

    /*
     * MapSqlParameterSource
     * */
    public int insertMapParameter(MapSqlParameterSource source) {
        return this.template.update("insert into member(id,name, point) values(:id,:name,:point)",
            source);
    }

    /*
     * BeanPropertySqlParameterSource
     * */
    public int insertBeanPropertySqlParameterSource(Member member) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(
            member);
        return this.template.update("insert into member(id,name, point) values(:id,:name,:point)",
            source);
    }

}
