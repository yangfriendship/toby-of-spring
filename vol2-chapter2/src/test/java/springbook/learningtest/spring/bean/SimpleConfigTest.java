package springbook.learningtest.spring.bean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.learningtest.spring.bean.SimpleConfig.SimpleHello;
import springbook.learningtest.spring.util.BeanDefinitionUtils;

public class SimpleConfigTest {

    /*
     * 예제는 @Configuration 을 통해서 작성 했다.
     * @Configuration 는 <context:annotation-config> 를 사용하는 효과가 있다.
     * xml 을 통해서 bean 을 관리하고 빈 후처리기 기능을 사용한다면 <context:annotation-config> 을 사용해야한다.
     * <context:annotation-config> 가 직접 빈후처리기의 역할을 하는 것이 아니라, 역할을 하는 빈을 자동으로 등록해준다.
     * ConfigurationClassPostProcessor$ImportAwareBeanPostProcessor
     * ConfigurationClassPostProcessor: @Configuration @Bean 을 찾아 등록해준다.
     * AutowiredAnnotationBeanPostProcessor: @Autowired 를 찾아 Bean 으로 등록해준다.
     * RequiredAnnotationBeanPostProcessor
     * PersistenceAnnotationBeanPostProcessor
     * <context:component-scan> 은 <context:annotation-config> 의 기능을 포함하고 있다.
     * */
    @Test
    public void postConstructTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SimpleConfig.class);

        SimpleHello hello = context.getBean("hello", SimpleHello.class);
        SimpleConfig config = context.getBean(SimpleConfig.class);
        assertNotNull(hello, "빈으로 등록되어야 한다.");
        assertNotNull(config, "Configuration 도 빈으로 빈으로 등록되어야 한다.");
        assertNotNull(hello.getCratedAt(), "PostConstruct 가 실행되며 프로퍼티가 생성되어야한다");
    }

    /*
    * Bean Role
    * 0: Application
    * 1: Support
    * 2: Infrastructure
    * */
    @Test
    public void beanDefinitionUtilPrintInfo() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SimpleConfig.class);

        BeanDefinitionUtils.printBeanDefinitionsForAnnotation(context);
    }

}
