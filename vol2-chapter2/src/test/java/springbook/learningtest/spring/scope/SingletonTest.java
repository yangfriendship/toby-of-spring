package springbook.learningtest.spring.scope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        @Bean
        public PrototypeBeanClient prototypeBeanClient() {
            PrototypeBeanClient beanClient = new PrototypeBeanClient();
            beanClient.setBean1(prototypeBean());
            beanClient.setBean2(prototypeBean());
            return beanClient;
        }

    }

    static class SingletonBean {

    }

    @Component
    static class SingletonBeanClient {

        @Autowired
        SingletonBean bean1;
        @Autowired
        SingletonBean bean2;

        public SingletonBean getBean1() {
            return bean1;
        }

        public void setBean1(SingletonBean bean1) {
            this.bean1 = bean1;
        }

        public SingletonBean getBean2() {
            return bean2;
        }

        public void setBean2(SingletonBean bean2) {
            this.bean2 = bean2;
        }
    }

    @Component
    static class PrototypeBeanClient {

        @Autowired
        PrototypeBean bean1;
        @Autowired
        PrototypeBean bean2;

        public PrototypeBean getBean1() {
            return bean1;
        }

        public void setBean1(PrototypeBean bean1) {
            this.bean1 = bean1;
        }

        public PrototypeBean getBean2() {
            return bean2;
        }

        public void setBean2(PrototypeBean bean2) {
            this.bean2 = bean2;
        }
    }

    static class PrototypeBean {

    }

    @Test
    public void singletonScope() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SingletonConfig.class);

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

        PrototypeBean bean1 = context.getBean("prototypeBean", PrototypeBean.class);
        PrototypeBean bean2 = context.getBean("prototypeBean", PrototypeBean.class);
        assertNotSame(bean1, bean2);
        assertNotNull(bean1);
        assertTrue(bean1 instanceof PrototypeBean);
        assertNotNull(bean1);
        assertTrue(bean2 instanceof PrototypeBean);

        PrototypeBeanClient client = context.getBean(PrototypeBeanClient.class);
        assertNotSame(client.getBean1(), client.getBean2());
    }

}
