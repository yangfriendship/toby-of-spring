package springbook.learningtest.spring.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import springbook.learningtest.spring.ioc.bean.annotation.AnnotatedHello;
import springbook.learningtest.spring.ioc.bean.annotation.AnnotatedHelloConfig;
import springbook.learningtest.spring.ioc.bean.annotation.HelloCollection;
import springbook.learningtest.spring.ioc.bean.annotation.MethodAutowireHelloConfig;
import springbook.learningtest.spring.ioc.bean.example.SystemPropertiesConfig;
import springbook.learningtest.spring.ioc.bean.example.QualifierHelloCollection;
import springbook.learningtest.spring.ioc.bean.example.QualifierHelloService;
import springbook.learningtest.spring.ioc.bean.example.NotConfigAnnotatedConfig;
import springbook.learningtest.spring.ioc.bean.example.ResourceHello;

public class ApplicationContextTest {

    @Test
    public void staticApplicationContext() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello1", Hello.class);

        Hello hello1 = context.getBean("hello1", Hello.class);
        assertNotNull(hello1);
    }

    @Test
    public void rootBeanDefinition() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello1", Hello.class);

        RootBeanDefinition definition = new RootBeanDefinition(Hello.class);
        definition.getPropertyValues().addPropertyValue("name", "Spring");

        context.registerBeanDefinition("hello2", definition);

        Hello hello1 = context.getBean("hello1", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);

        assertNotSame(hello1, hello2);
        assertEquals(hello2.getName(), "Spring");

        String[] beanDefinitionNames = context.getBeanFactory().getBeanDefinitionNames();
        assertEquals(2, beanDefinitionNames.length);
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }

    }

    @Test
    public void registerBeanWithDependencyTest() {
        // register
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("printer", StringPrinter.class);

        RootBeanDefinition helloBean = new RootBeanDefinition(Hello.class);
        helloBean.getPropertyValues().addPropertyValue("name", "Spring");
        helloBean.getPropertyValues()
            .addPropertyValue("printer", new RuntimeBeanReference("printer"));

        context.registerBeanDefinition("hello", helloBean);
        // get
        Hello hello = context.getBean("hello", Hello.class);
        assertNotNull(hello);
        assertNotNull(hello.getPrinter());
        assertNotNull(hello.getName());
        Printer printer = hello.getPrinter();
        assertTrue(printer instanceof StringPrinter);

        Printer getBeanPrinter = context.getBean("printer", Printer.class);
        assertSame(getBeanPrinter, printer);
    }

    @Test
    public void genericApplicationContestTest() {
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(
            "/genericApplicationContext.xml"); // file, http, classpath(default) 모두 가능
        context.refresh(); // 메타 정보 입력완료! 어플리케이션 컨텍스트 초기화 명령!

        Hello hello = context.getBean("hello", Hello.class);
        assertNotNull(hello);
        Printer printer = hello.getPrinter();
        assertNotNull(printer);
        assertTrue(printer instanceof StringPrinter);
    }

    @Test
    public void genericXmlApplicationContext() {
//        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
//            "/genericApplicationContext.xml");
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load("/genericApplicationContext.xml");
        context.refresh();

        Hello hello = context.getBean("hello", Hello.class);
        assertNotNull(hello);
        Printer printer = hello.getPrinter();
        assertNotNull(printer);
        assertTrue(printer instanceof StringPrinter);
    }

    @Test
    public void parentChildContextTest() {
        GenericXmlApplicationContext parentContext = new GenericXmlApplicationContext(
            "/parentContext.xml");

        GenericApplicationContext childContext = new GenericApplicationContext(
            parentContext);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(childContext);
        reader.loadBeanDefinitions("/childContext.xml");
        childContext.refresh();

        Hello hello = childContext.getBean("hello", Hello.class);
        assertNotNull(hello);
        assertEquals("Hello child", hello.sayHello());
    }

    @Test
    public void simpleBeanScanning() {
        ApplicationContext context = new AnnotationConfigApplicationContext(
            "springbook.learningtest.spring.ioc.bean.annotation");
        AnnotatedHello hello = context.getBean(AnnotatedHello.class);
        AnnotatedHello namedHello = context.getBean("woojungHello", AnnotatedHello.class);
        assertNotNull(hello);
        assertNotNull(namedHello);
        assertSame(hello, namedHello);
    }

    @Test
    public void configurationBeanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            AnnotatedHelloConfig.class);

        Hello hello1 = context.getBean("hello1", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);
        assertNotNull(hello1);
        assertNotNull(hello2);

        assertNotSame(hello1, hello2);
        assertSame(hello1.getPrinter(), hello2.getPrinter());

        AnnotatedHelloConfig configBean = context.getBean(AnnotatedHelloConfig.class);
        assertNotNull(configBean);
    }

    @Test
    public void notConfigurationBeanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            NotConfigAnnotatedConfig.class);

        Hello hello1 = context.getBean("hello1", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);

        assertNotSame(hello1.getPrinter(), hello2.getPrinter());

        Hello sameHello1 = context.getBean("sameHello1", Hello.class);
        Hello sameHello2 = context.getBean("sameHello2", Hello.class);

        assertSame(sameHello1.getPrinter(), sameHello2.getPrinter());
    }

    @Test
    public void constructorBeanTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        Hello hello = context.getBean("constructorHello", Hello.class);
        assertNotNull(hello);
    }

    @Test
    public void autowirePropertyBeanTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        Hello hello = context.getBean("autowireHello", Hello.class);
        Printer printer = context.getBean("printer", Printer.class);
        assertNotNull(hello);
        assertNotNull(hello.getPrinter());
        assertSame(hello.getPrinter(), printer);
        assertEquals("autowireHello", hello.getName());
    }

    @Test
    public void resourceBeanTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");
        ResourceHello resourceHello = context.getBean("resourceHello", ResourceHello.class);

        assertNotNull(resourceHello.getConsolePrinter());
        assertNotNull(resourceHello.getStringPrinter());

        assertTrue(resourceHello.getConsolePrinter() instanceof ConsolePrinter);
        assertTrue(resourceHello.getStringPrinter() instanceof StringPrinter);

        Printer stringPrinter = context.getBean("stringPrinter", Printer.class);
        Printer consolePrinter = context.getBean("consolePrinter", Printer.class);

        assertSame(resourceHello.getConsolePrinter(), consolePrinter);
        assertSame(resourceHello.getStringPrinter(), stringPrinter);
    }

    @Test
    public void collectionBeans() {
        ApplicationContext context = new AnnotationConfigApplicationContext(
            NotConfigAnnotatedConfig.class);
        HelloCollection hellos = context.getBean(HelloCollection.class);
        assertEquals(4, hellos.getHelloList().size());

        Map<String, Hello> helloMap = hellos.getHelloMap();
    }

    @Test
    public void qualifierTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        QualifierHelloService helloService = context.getBean("qualifierHelloService",
            QualifierHelloService.class);
        Hello hello = context.getBean("autowireHello", Hello.class);
        assertSame(hello, helloService.getHello());
    }

    @Test
    public void qualifierInClassTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        QualifierHelloCollection helloService = context.getBean("qualifierHelloCollection",
            QualifierHelloCollection.class);
        List<Hello> hellos = helloService.getHellos();
        assertEquals(2, hellos.size());
        for (Hello hello : hellos) {
            assertEquals("Qualifier Hello!", hello.getName());
        }
    }

    @Test
    public void methodAutowireTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            MethodAutowireHelloConfig.class);

        Hello hello = context.getBean("methodHello", Hello.class);
        assertNotNull(hello);
        assertTrue(hello.getPrinter() instanceof ConsolePrinter);
    }

    @Test
    public void collectionPropertiesTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            MethodAutowireHelloConfig.class);

        List<String> names = context.getBean("names", List.class);

        assertNotNull(names);
        assertEquals(4, names.size());
    }

    @Test
    public void xmlBeanListTest() {
        ApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        List list = context.getBean("printerBeans", List.class);
        assertNotNull(list);
        assertEquals(3, list.size());

    }

    @Test
    public void systemEnvironmentAndPropertyTest() {
        ApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");
        Map<String, String> systemProperties = context.getBean("systemProperties", Map.class);
        Map systemEnvironment = context.getBean("systemEnvironment", Map.class);

        assertNotNull(systemEnvironment);
        assertFalse(systemEnvironment.isEmpty());
        assertNotNull(systemProperties);
        assertFalse(systemProperties.isEmpty());
    }

    @Test
    public void systemPropertiesAndValueDiTest() {
        ApplicationContext context = new GenericXmlApplicationContext(
            "/genericApplicationContext.xml");

        SystemPropertiesConfig bean = context.getBean(SystemPropertiesConfig.class);

        assertNotNull(bean);
        assertNotNull(bean.getOsName());
    }

}
