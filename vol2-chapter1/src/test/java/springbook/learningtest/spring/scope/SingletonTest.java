package springbook.learningtest.spring.scope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class SingletonTest {

    @Configuration
    static class SingletonConfig {

        @Bean
        public SingletonBeanClient singletonBeanClient() {
            return new SingletonBeanClient();
        }

        @Bean
        public SingletonBean bean() {
            return new SingletonBean();
        }

        @Bean
        @Scope(value = "prototype")
        public PrototypeBean prototypeBean() {
            return new PrototypeBean();
        }
    }

    static class SingletonBean {

    }

    @Component
    @Getter
    @Setter
    static class SingletonBeanClient {

        @Autowired
        SingletonBean bean1;
        @Autowired
        SingletonBean bean2;
    }

    static class PrototypeBean {

    }

    @Test
    public void singletonScope() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SingletonConfig.class);

        Set<SingletonBean> singletonBeans = new HashSet<>();

        SingletonBean bean1 = context.getBean("bean", SingletonBean.class);
        SingletonBean bean2 = context.getBean("bean", SingletonBean.class);
        assertSame(bean1, bean2);

        SingletonBeanClient client = context.getBean("singletonBeanClient",
            SingletonBeanClient.class);

        SingletonBean bean3 = client.getBean1();
        SingletonBean bean4 = client.getBean2();
        assertEquals(bean3, bean4);
        assertEquals(bean1, bean3);
    }

    @Test
    public void prototypeScope() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SingletonConfig.class);

        PrototypeBean bean1 = context.getBean( PrototypeBean.class);
        PrototypeBean bean2 = context.getBean( PrototypeBean.class);
        assertNotSame(bean1, bean2);
    }

}
