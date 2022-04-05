package springbook.learningtest.spring.ioc.bean.annotation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.Hello;

@Component
public class HelloCollection {

    @Autowired
    @Getter
    public List<Hello> helloList;
    // key is bean's name
    @Getter
    @Autowired
    private Map<String, Hello> helloMap;

}
