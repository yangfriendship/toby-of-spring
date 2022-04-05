package springbook.learningtest.spring.ioc.profileproperty;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class ApplicationContextInitializerImpl implements
    ApplicationContextInitializer<AnnotationConfigApplicationContext> {

    @Override
    public void initialize(AnnotationConfigApplicationContext ctx) {
        System.out.println("ApplicationContextInitializerImpl is working");
        ConfigurableEnvironment env = ctx.getEnvironment();
        Map<String, Object> properties = new HashMap<>();
        properties.put("props.name", "woojung");
        env.getPropertySources().addFirst(new MapPropertySource("my", properties));
    }

}
