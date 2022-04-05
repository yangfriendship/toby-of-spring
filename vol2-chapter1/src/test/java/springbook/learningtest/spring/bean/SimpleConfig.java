package springbook.learningtest.spring.bean;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.ioc.Hello;

@Configuration
public class SimpleConfig {

    @Autowired
    SimpleHello simpleHello;

    @Bean
    public SimpleHello hello() {
        return new SimpleHello();
    }

    @Getter
    public static class SimpleHello {
        private LocalDateTime cratedAt;

        @PostConstruct
        public void init() {
            this.cratedAt = LocalDateTime.now();
        }
    }


}
