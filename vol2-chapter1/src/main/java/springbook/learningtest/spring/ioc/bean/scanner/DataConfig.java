package springbook.learningtest.spring.ioc.bean.scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public SomeDataAccessBean someDataAccessBean() {
        return new SomeDataAccessBean();
    }

    public static class SomeDataAccessBean {

    }
}
