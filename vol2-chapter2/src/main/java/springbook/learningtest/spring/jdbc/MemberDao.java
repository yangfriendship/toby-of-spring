package springbook.learningtest.spring.jdbc;

import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import springbook.learningtest.spring.jdbc.entity.Member;

public class MemberDao {

    private SimpleJdbcTemplate template;

    public void setJdbcTemplate(SimpleJdbcTemplate template) {
        this.template = template;
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

    public int countMemberQueryForInt(String name) {
        return this.template.queryForInt("select count(*) from member");
    }

}
