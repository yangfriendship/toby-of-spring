package springbook.learningtest.spring.ioc.bean.example;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.Hello;

/*
* 1. Qualifier 로 지정된 bean 을 찾는다.
* 2. 존재하지 않는다면 같은 이름의 빈을 찾는다.
* 3. Qualifier > Bean Name
* */
@Component
public class QualifierHelloCollection {

    @Autowired
    @Qualifier("qualifierHello")
    @Getter
    private List<Hello> hellos;

}
