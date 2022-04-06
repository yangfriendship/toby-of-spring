package springbook.learningtest.spring.jdbc.config;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceConfig.class)
public class DataSourceConfigTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void init() {
        assertNotNull(this.dataSource, "DataSource 가 정상적으로 생성되어야 한다.");
        assertDoesNotThrow(() -> {
            assertNotNull(this.dataSource.getConnection());
        }, "Connection 을 반환해줘야 한다.");
    }

}