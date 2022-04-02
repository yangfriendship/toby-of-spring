package springbook;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.TestUserServiceImpl;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.sqlservice.EmbeddedSqlRegistry;
import springbook.user.sqlservice.HashMapSqlRegistry;
import springbook.user.sqlservice.JaxbSqlReader;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlReader;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

@Configuration
@EnableTransactionManagement // <tx:annotation-driven/>
@ComponentScan(basePackages = "springbook.user")
public class TestApplicationContext {

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
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public SqlRegistry hashMapSqlRegistry() {
        return new HashMapSqlRegistry();
    }

    @Bean
    public SqlReader sqlReader() {
        JaxbSqlReader sqlReader = new JaxbSqlReader();
        sqlReader.setSqlmapFile("/sqlmap.xml");
        return sqlReader;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setSqlRegistry(embeddedSqlRegistry());
        sqlService.setUnmarshaller(unmarshaller());
        return sqlService;
    }

    @Bean
    public UserDao userDaoJdbc() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        return userDaoJdbc;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public TestUserServiceImpl testUserService() {
        TestUserServiceImpl testUserService = new TestUserServiceImpl();
//        testUserService.setUserDao(this.userDao);
        testUserService.setUserDao(userDaoJdbc());
        testUserService.setMailSender(mailSender());
        testUserService.setId("woojung3");
        return testUserService;
    }

    @Bean
    public SqlRegistry embeddedSqlRegistry() {
        EmbeddedSqlRegistry sqlRegistry = new EmbeddedSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public EmbeddedDatabase embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:sql/sqlRegistrySchema.sql")
            .build();
    }

}
