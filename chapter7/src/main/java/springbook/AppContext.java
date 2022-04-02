package springbook;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.TestUserServiceImpl;

@Configuration
@EnableTransactionManagement // <tx:annotation-driven/>
@ComponentScan(basePackages = "springbook.user")
@Import({SqlServiceContext.class})
public class AppContext {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springbook");
        dataSource.setUsername("youzheng");
        dataSource.setPassword("youzheng123");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public UserDao userDaoJdbc() {
        return new UserDaoJdbc();
    }

    @Configuration
    @Profile("!product")
    public static class TestAppContext {

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }

        @Bean
        public TestUserServiceImpl testUserService() {
            TestUserServiceImpl testUserService = new TestUserServiceImpl();
            testUserService.setMailSender(mailSender());
            testUserService.setId("woojung3");
            return testUserService;
        }
    }

    @Profile("product")
    @Configuration
    public static class ProductAppContext {

        @Bean
        public JavaMailSender mailSender() {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            // set info
            return sender;
        }

    }

}
