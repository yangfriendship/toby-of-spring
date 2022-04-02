package springbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.TestUserServiceImpl;

@Configuration
public class TestAppContext {

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public TestUserServiceImpl testUserService() {
        TestUserServiceImpl testUserService = new TestUserServiceImpl();
//        testUserService.setUserDao(this.userDao);
//        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(mailSender());
        testUserService.setId("woojung3");
        return testUserService;
    }
}
