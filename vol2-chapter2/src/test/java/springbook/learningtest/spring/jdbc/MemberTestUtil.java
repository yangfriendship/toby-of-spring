package springbook.learningtest.spring.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import springbook.learningtest.spring.jdbc.entity.Member;

public class MemberTestUtil {

    private final SimpleJdbcTemplate jdbcTemplate;
    private int currentIndex = 1;

    public MemberTestUtil(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getNextIndex() {
        return this.currentIndex;
    }

    public Member insertOnMember() {
        int nextIndex = currentIndex + 1;
        Member member = new Member();
        member.setId(nextIndex);
        member.setName("member" + currentIndex);
        member.setPoint(2.3 + nextIndex);
        currentIndex++;
        return insertOnMember(member);
    }

    Member insertOnMember(Member member) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(member);
        this.jdbcTemplate.update("insert into member(id,name, point) values(:id,:name,:point)",
            source);
        return member;
    }

    public Member findById(int id) {
        return this.jdbcTemplate.queryForObject("select * from member where id = ?",
            (RowMapper<Member>) (rs, rowNum) -> new Member(rs.getInt("id"), rs.getString("name"),
                rs.getDouble("point")), id);
    }

    public void assertEqualsMember(Member source, Member target) {
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getPoint(), target.getPoint());
    }
}