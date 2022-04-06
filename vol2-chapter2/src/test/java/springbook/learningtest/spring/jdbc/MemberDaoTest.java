package springbook.learningtest.spring.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        Member member = new Member();
        member.setId(30);
        member.setName("insertTest");
        member.setPoint(2.3);
        int affectedRowCount = this.memberDao.insertBeanPropertySqlParameterSource(member);
        assertEquals(1, affectedRowCount);
    }

}