package springbook.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.dao.connectionmaker.ConnectionMaker;
import springbook.user.dao.connectionmaker.SimpleConnectionMaker;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        UserDao userDaoJdbc = new UserDaoJdbc();
//        userDaoJdbc.set(dataSource());
        return userDaoJdbc;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new SimpleConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springbook");
        dataSource.setUsername("youzheng");
        dataSource.setPassword("youzheng123");
        return dataSource;
    }

}
