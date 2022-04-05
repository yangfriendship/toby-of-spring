package springbook.learningtest.spring.ioc.profileproperty;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

public class LocalProfile implements SayCurrentProfile {

    @Getter
    @Setter
    @Value("${current}")
    private String current;

    @Override
    public String sayProfile() {
        return this.current;
    }
}
