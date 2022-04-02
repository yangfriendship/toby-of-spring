package springbook;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@PropertySource("/datasource.properties")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(
        @Value("${db.driverClass}") Class<? extends Driver> driverClass,
        @Value("${db.url}") String url,
        @Value("${db.username}") String username,
        @Value("${db.password}") String password
    ) {
        SimpleDriverDataSource source = new SimpleDriverDataSource();
        source.setDriverClass(driverClass);
        source.setUrl(url);
        source.setUsername(username);
        source.setPassword(password);
        return source;
    }

    /*
     * @Value 를 통해 값을 주입받으려면 PropertySourcesPlaceholder 를 빈으로 등록해야한다.
     * 반드시 static 메소드로 선언해야 한다!
     * */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
