package springbook.learningtest.spring.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import springbook.learningtest.spring.jdbc.config.AppConfig;
import springbook.learningtest.spring.jdbc.entity.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = AppConfig.class)
public class SimpleJdbcInsertTest {

    @Autowired
    SimpleJdbcInsert jdbcInsert;
    @Autowired
    SimpleJdbcTemplate template;
    MemberTestUtil memberTestUtil;

    @Before
    public void setup() {
        this.memberTestUtil = new MemberTestUtil(this.template);
    }

    @Test
    public void withTableNameTest() {
        SimpleJdbcInsert insert = this.jdbcInsert.withTableName("member");
        Member member = new Member(30, "woojung", 2.3);
        int execute = insert.execute(new BeanPropertySqlParameterSource(member));

        assertEquals(1, execute);
        Member fromDb = this.memberTestUtil.findById(member.getId());
        this.memberTestUtil.assertEqualsMember(member, fromDb);
    }

}
