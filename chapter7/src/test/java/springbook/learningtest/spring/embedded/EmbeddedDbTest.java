package springbook.learningtest.spring.embedded;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

public class EmbeddedDbTest {

    EmbeddedDatabase database;
    SimpleJdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.database = new EmbeddedDatabaseBuilder()
            .setType(H2)
            .addScript("classpath:/sql/schema.sql")
            .addScript("classpath:/sql/data.sql")
            .build();
        this.jdbcTemplate = new SimpleJdbcTemplate(this.database);
    }

    @After
    public void tearDown() {
        this.database.shutdown();
    }

    @Test
    public void initData() {
        int count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM SQLMAP");
        assertEquals(2, count);

        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(
            "SELECT * FROM SQLMAP ORDER BY KEY_");

        assertEquals("KEY1",result.get(0).get("KEY_"));
        assertEquals("SQL1",result.get(0).get("SQL_"));
        assertEquals("KEY2",result.get(1).get("KEY_"));
        assertEquals("SQL2",result.get(1).get("SQL_"));
    }

}
