package springbook.learningtest.spring.jdbc.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("/datasource.properties")
public class DataSourceConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }

    @Bean
    public SimpleJdbcTemplate jdbcTemplate() {
        return new SimpleJdbcTemplate(this.dataSource);
    };

    @Bean
    public DataSource dataSource(
        @Value("${db.driverClass}") String driverClass,
        @Value("${db.url}") String url,
        @Value("${db.username}") String username,
        @Value("${db.password}") String password
    ) throws ClassNotFoundException {
        Class aClass = Class.forName(driverClass);
        SimpleDriverDataSource source = new SimpleDriverDataSource();
        source.setPassword(password);
        source.setDriverClass(aClass);
        source.setUsername(username);
        source.setUrl(url);
        return source;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        return transactionManager;
    }

}
