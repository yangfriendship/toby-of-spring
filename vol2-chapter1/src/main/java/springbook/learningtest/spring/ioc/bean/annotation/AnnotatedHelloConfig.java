package springbook.learningtest.spring.ioc.bean.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.ioc.Hello;
import springbook.learningtest.spring.ioc.Printer;
import springbook.learningtest.spring.ioc.StringPrinter;

@Configuration
public class AnnotatedHelloConfig {

    @Bean
    public Hello hello1() {
        Hello hello = new Hello();
        hello.setPrinter(stringPrinter());
        hello.setName("Youzheng");
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setPrinter(stringPrinter());
        hello.setName("Youzheng");
        return hello;
    }

    @Bean
    public Printer stringPrinter() {
        return new StringPrinter();
    }

}
