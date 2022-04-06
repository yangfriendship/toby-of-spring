package springbook.learningtest.spring.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    private MemberTestUtil memberTestUtil;
    @Autowired
    MemberDao memberDao;
    @Autowired
    SimpleJdbcTemplate template;

    @Before
    public void setup() {
        this.memberTestUtil = new MemberTestUtil(this.template);
    }

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
        memberTestUtil.insertOnMember();
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
        this.memberTestUtil.insertOnMember();
        this.memberTestUtil.insertOnMember();
        this.memberTestUtil.insertOnMember();

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
        Member member = memberTestUtil.insertOnMember();

        String name = this.template.queryForObject("select name from member where id = ?",
            String.class, member.getId());

        assertEquals(member.getName(), name);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void queryForObjectTest_EmptyResultDataAccessException() {
        Member member = memberTestUtil.insertOnMember();

        String name = this.template.queryForObject("select name from member where id = ?",
            String.class, "HIYO~");

        assertEquals(member.getName(), name);
    }

    @Test
    public void queryForObjectTest_BeanPropertyRowMapper() {
        Member member = memberTestUtil.insertOnMember();

        Member result = this.template.queryForObject(
            "select id,name,point from member where id = ?",
            new BeanPropertyRowMapper<>(Member.class), member.getId());

        this.memberTestUtil.assertEqualsMember(member, result);
    }

    @Test
    public void queryForObjectTest_implementRowMapper() {
        Member member = this.memberTestUtil.insertOnMember();

        Member result = this.template.queryForObject(
            "select id,name,point from member where id = ?",
            (RowMapper<Member>) (rs, rowNum) -> {
                Member result1 = new Member();
                result1.setId(rs.getInt("id"));
                result1.setName(rs.getString("name"));
                result1.setPoint(rs.getDouble("point"));
                return result1;
            }, member.getId());

        this.memberTestUtil.assertEqualsMember(member, result);
    }

    /*
     * 해당하는 결과가 없더라도 List 는 null 이 아니다. size == 0 인 초기화된 List 를 반환해준다.
     * */
    @Test
    public void queryTest() {
        Member member = this.memberTestUtil.insertOnMember();

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
        this.memberTestUtil.assertEqualsMember(member, findMember);
    }

    /*
     * 하나 이상의 로우를 반환해야 한다. EmptyResultDataAccessException
     * */
    @Test
    public void queryForMapTest() {
        Member member = this.memberTestUtil.insertOnMember();
        Map<String, Object> result = this.template.queryForMap(
            "select * from member where id = ?", member.getId());
        Member fromDb = new Member((Integer) result.get("id"), (String) result.get("name"),
            (Double) result.get("point"));

        this.memberTestUtil.assertEqualsMember(member, fromDb);
    }

    /*
     * queryForObject 와 마찬가지로 row 를 꼭 반환해야한다. 값이 존재하지 않는다면 EmptyResultDataAccessException
     * */
    @Test(expected = EmptyResultDataAccessException.class)
    public void queryForMapTest_EmptyResultDataAccessException() {
        Member member = this.memberTestUtil.insertOnMember();
        Map<String, Object> result = this.template.queryForMap(
            "select * from member where id = ?", "KKKKK");
    }

    @Test
    public void queryForList() {
        int nextIndex = this.memberTestUtil.getNextIndex();
        Member member1 = this.memberTestUtil.insertOnMember();
        Member member2 = this.memberTestUtil.insertOnMember();

        List<Map<String, Object>> result = this.template.queryForList(
            "select * from member where  id >  ? order by id", nextIndex);

        assertEquals(2, result.size());
        this.memberTestUtil.assertEqualsMember(member1,
            new Member((Integer) result.get(0).get("id"), (String) result.get(0).get("name"),
                (Double) result.get(0).get("point")));

        this.memberTestUtil.assertEqualsMember(member2,
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

}