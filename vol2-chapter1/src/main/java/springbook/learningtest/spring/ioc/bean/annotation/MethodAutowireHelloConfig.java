package springbook.learningtest.spring.ioc.bean.annotation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.ioc.ConsolePrinter;
import springbook.learningtest.spring.ioc.Hello;
import springbook.learningtest.spring.ioc.Printer;
import springbook.learningtest.spring.ioc.StringPrinter;

@Configuration
public class MethodAutowireHelloConfig {

    @Bean
    public Hello methodHello(@Qualifier("consolePrinterForMethod") Printer printer) {
        Hello hello = new Hello();
        hello.setPrinter(printer);
        return hello;
    }

    @Bean
    public Printer consolePrinterForMethod() {
        Printer consolePrinter = new ConsolePrinter();
        return consolePrinter;
    }

    @Bean
    public Printer stringBuilderPrinterForMethod() {
        Printer consolePrinter = new StringPrinter();
        return consolePrinter;
    }

    @Bean
    public List<String> names() {
        ArrayList<String> names = new ArrayList<>();
        names.add("woojung1");
        names.add("woojung2");
        names.add("woojung3");
        names.add("woojung4");
        return names;
    }

}
