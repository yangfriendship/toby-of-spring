package springbook.learningtest.spring.ioc.bean.example;

import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.Hello;
import springbook.learningtest.spring.ioc.Printer;
import springbook.learningtest.spring.ioc.StringPrinter;
import springbook.learningtest.spring.ioc.bean.annotation.HelloCollection;

@Component
public class NotConfigAnnotatedConfig {

    @Setter
    private Printer printer;

    @Bean
    public Hello hello1() {
        Hello hello = new Hello();
        hello.setPrinter(stringPrinter());
        hello.setName("hello1");
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setPrinter(stringPrinter());
        hello.setName("hello2");
        return hello;
    }

    @Bean
    public Printer stringPrinter() {
        return new StringPrinter();
    }

    @Bean
    public Hello sameHello1() {
        Hello hello = new Hello();
        hello.setPrinter(this.printer);
        hello.setName("same hello1");
        return hello;
    }

    @Bean
    public Hello sameHello2() {
        Hello hello = new Hello();
        hello.setPrinter(this.printer);
        hello.setName("same hello2");
        return hello;
    }

    @Bean
    public HelloCollection helloCollection() {
        return new HelloCollection();
    }

}
