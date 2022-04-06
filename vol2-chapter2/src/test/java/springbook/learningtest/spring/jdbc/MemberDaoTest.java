package springbook.learningtest.spring.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import springbook.learningtest.spring.jdbc.config.AppConfig;
import springbook.learningtest.spring.jdbc.entity.Member;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MemberDaoTest {

    @Autowired
    MemberDao memberDao;
    @Autowired
    SimpleJdbcTemplate template;

    @Test
    public void initTest() {
        assertNotNull(this.memberDao);
    }

    /*
     * ? 를 이용한 가변인자 파라미터 전달
     * */
    @Test
    public void insertTest_sqlParameter() throws Exception {
        int affectedRowCount = this.memberDao.insertSqlParameter(30, "insertTest", 2.3);
        assertEquals(1, affectedRowCount);
    }

    @Test
    public void insertTest_placeholder() throws Exception {
        int affectedRowCount = this.memberDao.insertSqlParameter(30, "insertTest", 2.3);
        assertEquals(1, affectedRowCount);
    }

    @Test
    public void insertTest_mapParameterSource() throws Exception {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", 30);
        source.addValue("name", "insertTest");
        source.addValue("point", 2.3);

        int affectedRowCount = this.memberDao.insertMapParameter(source);
        assertEquals(1, affectedRowCount);
    }

    @Test
    public void insertTest_beanPropertySqlParameterSource() throws Exception {
        insertOnMember();
    }

    @Test
    public void countMemberQueryForIntTest() {
        Member member = new Member();
        member.setId(30);
        member.setName("insertTest");
        member.setPoint(2.3);
        int affectedRowCount = this.memberDao.insertBeanPropertySqlParameterSource(member);
        assertEquals(1, affectedRowCount);

        int count = this.memberDao.countMemberQueryForInt(member.getName());
        assertNotEquals(0, count);
    }

    @Test
    public void queryForInt_mapSqlParameterSource() {

        int min = this.template.queryForInt("select count(*) from member where point > :min;",
            new MapSqlParameterSource("min", 0));

        assertTrue(min > 0);
    }


    /*
     * queryForObject 는 1개의 Column 의 값을 2번째 인자에 오는 값으로 받아온다.
     * 만약 값이 없다면, EmptyResultDataAccessException 이 발생
     * */
    @Test
    public void queryForObjectTest() {
        Member member = insertOnMember();

        String name = this.template.queryForObject("select name from member where id = ?",
            String.class, member.getId());

        assertEquals(member.getName(), name);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void queryForObjectTest_EmptyResultDataAccessException() {
        Member member = insertOnMember();

        String name = this.template.queryForObject("select name from member where id = ?",
            String.class, "HIYO~");

        assertEquals(member.getName(), name);
    }

    @Test
    public void queryForObjectTest_BeanPropertyRowMapper() {
        Member member = insertOnMember();

        Member result = this.template.queryForObject(
            "select id,name,point from member where id = ?",
            new BeanPropertyRowMapper<>(Member.class), member.getId());

        assertEqualsMember(member, result);
    }

    @Test
    public void queryForObjectTest_implementRowMapper() {
        Member member = insertOnMember();

        Member result = this.template.queryForObject(
            "select id,name,point from member where id = ?",
            (RowMapper<Member>) (rs, rowNum) -> {
                Member result1 = new Member();
                result1.setId(rs.getInt("id"));
                result1.setName(rs.getString("name"));
                result1.setPoint(rs.getDouble("point"));
                return result1;
            }, member.getId());

        assertEqualsMember(member, result);
    }

    /*
     * 해당하는 결과가 없더라도 List 는 null 이 아니다. size == 0 인 초기화된 List 를 반환해준다.
     * */
    @Test
    public void queryTest() {
        Member member = insertOnMember();

        List<Member> result = this.template.query(
            "select id,name,point from member where id = ?",
            (RowMapper<Member>) (rs, rowNum) -> {
                Member result1 = new Member();
                result1.setId(rs.getInt("id"));
                result1.setName(rs.getString("name"));
                result1.setPoint(rs.getDouble("point"));
                return result1;
            }, member.getId());

        assertNotEquals(0, result.size());
        Member findMember = result.stream().filter(m -> m.getId() == member.getId()).findFirst()
            .get();
        assertEqualsMember(member, findMember);
    }

    /*
     * 하나 이상의 로우를 반환해야 한다. EmptyResultDataAccessException
     * */
    @Test
    public void queryForMapTest() {
        Member member = insertOnMember();
        Map<String, Object> result = this.template.queryForMap(
            "select * from member where id = ?", member.getId());
        Member fromDb = new Member((Integer) result.get("id"), (String) result.get("name"),
            (Double) result.get("point"));

        assertEqualsMember(member, fromDb);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void queryForMapTest_EmptyResultDataAccessException() {
        Member member = insertOnMember();
        Map<String, Object> result = this.template.queryForMap(
            "select * from member where id = ?", "KKKKK");
    }

    @Test
    public void queryForList() {
        Member member1 = insertOnMember();
        Member member2 = insertOnMember(new Member(40, "member2", 3.3));

        List<Map<String, Object>> result = this.template.queryForList(
            "select * from member where  id >  ? order by id", 10);

        assertEquals(2, result.size());
        assertEqualsMember(member1,
            new Member((Integer) result.get(0).get("id"), (String) result.get(0).get("name"),
                (Double) result.get(0).get("point")));

        assertEqualsMember(member2,
            new Member((Integer) result.get(1).get("id"), (String) result.get(1).get("name"),
                (Double) result.get(1).get("point")));
    }

    /* Batch Method */
    @Test
    public void batchUpdate_sqlMapParameterSourceTest() {
        final int loopCount = 30;
        SqlParameterSource[] parameterSources = new SqlParameterSource[loopCount];
        for (int i = 0; i < loopCount; i++) {
            parameterSources[i] = new MapSqlParameterSource().addValue("id", 100 + i)
                .addValue("name", "woojung" + i)
                .addValue("point", 2.3 + i);
        }

        int[] result = this.template.batchUpdate(
            "insert into member(id,name,point) values(:id, :name, :point);",
            parameterSources
        );

        int count = this.template.queryForInt("select count(*) from member");
        assertTrue(count >= loopCount);
    }

    private void assertEqualsMember(Member source, Member target) {
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getPoint(), target.getPoint());
    }

    private Member insertOnMember() {
        Member member = new Member();
        member.setId(30);
        member.setName("insertTest");
        member.setPoint(2.3);
        return insertOnMember(member);
    }

    private Member insertOnMember(Member member) {
        int affectedRowCount = this.memberDao.insertBeanPropertySqlParameterSource(member);
        assertEquals(1, affectedRowCount);
        return member;
    }

}