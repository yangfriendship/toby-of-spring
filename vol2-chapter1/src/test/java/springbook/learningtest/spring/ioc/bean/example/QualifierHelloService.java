package springbook.learningtest.spring.ioc.bean.example;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.Hello;
import springbook.learningtest.spring.ioc.Printer;

@Component
@Getter
public class QualifierHelloService {

    @Autowired
    @Qualifier("autowireHello")
    private Hello hello;

}
