package springbook.learningtest.spring.ioc.bean.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.ioc.Hello;
import springbook.learningtest.spring.ioc.Printer;

@Configuration
public class QualifierHelloConfig {

    @Autowired
    private Printer printer;

    @Qualifier("qualifierHello")
    @Bean
    public Hello qualifierHello1() {
        Hello hello = new Hello();
        hello.setPrinter(printer);
        hello.setName("Qualifier Hello!");
        return hello;
    }

    @Qualifier("qualifierHello")
    @Bean
    public Hello qualifierHello2() {
        Hello hello = new Hello();
        hello.setPrinter(printer);
        hello.setName("Qualifier Hello!");
        return hello;
    }

}
