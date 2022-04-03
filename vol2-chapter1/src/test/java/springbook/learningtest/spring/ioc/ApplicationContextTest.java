package springbook.learningtest.spring.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

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

}
