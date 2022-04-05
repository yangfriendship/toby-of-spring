package springbook.learningtest.spring.ioc.bean.objectfactory;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class ClientObject {

    @Autowired
    public DependencyObject object;

}
