package springbook.learningtest.spring.ioc.scanner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.learningtest.spring.ioc.bean.scanner.AppConfig;
import springbook.learningtest.spring.ioc.bean.scanner.DataConfig;
import springbook.learningtest.spring.ioc.bean.scanner.DataConfig.SomeDataAccessBean;
import springbook.learningtest.spring.ioc.bean.scanner.SomethingBean;

public class ScannerTest {

    @Test
    public void scannerTesT() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig appConfig = ctx.getBean(AppConfig.class);
        assertNotNull(appConfig);
        SomethingBean bean1 = ctx.getBean("beanInClass", SomethingBean.class);
        SomethingBean bean2 = ctx.getBean("beanInClass", SomethingBean.class);
        assertNotNull(bean1);
        assertSame(bean1, bean2);
    }

    @Test
    public void importTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        DataConfig dataConfig = ctx.getBean(DataConfig.class);

        assertNotNull(dataConfig, "@Import 를 통해서 다른 컨피그레이션을 등록한다.");
        SomeDataAccessBean bean = ctx.getBean(SomeDataAccessBean.class);
        assertNotNull(bean, "해당 컨피그레이션을 통해 등록한 Bean 도 등록되어 있어야 한다.");
    }


}
