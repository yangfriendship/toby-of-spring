package springbook.user.dao.connectionmaker;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;

//@Configuration
public class CountingDaoFactory {

//    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(dataSource());
        return userDaoJdbc;
    }

//    @Bean
    public ConnectionMaker connectionMaker() {
        return new SimpleConnectionMaker();
    }

//    @Bean
    public ConnectionMaker countingConnectionMaker() {
        return new CountingConnectionMaker(connectionMaker());
    }

//    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springbook");
        dataSource.setUsername("youzheng");
        dataSource.setPassword("youzheng123");
        return dataSource;
    }

}
