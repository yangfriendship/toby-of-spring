package springbook.learningtest.spring.ioc.bean.example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SystemPropertiesConfig {

    @Value("#{systemProperties['os.name']}")
    public String osName;

}
