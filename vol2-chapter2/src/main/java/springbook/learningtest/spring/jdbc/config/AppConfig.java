package springbook.learningtest.spring.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import springbook.learningtest.spring.jdbc.MemberDao;

@Configuration
@Import(DataSourceConfig.class)
public class AppConfig {

    @Bean
    public MemberDao memberDao(SimpleJdbcTemplate jdbcTemplate) {
        MemberDao memberDao = new MemberDao();
        memberDao.setJdbcTemplate(jdbcTemplate);
        return memberDao;
    }

}
