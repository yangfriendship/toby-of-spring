package springbook.learningtest.jdk.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.learningtest.jdk.Hello;
import springbook.learningtest.jdk.HelloTarget;

public class DynamicProxyTest {

    @Test
    public void simpleProxy() throws Exception {
        Hello helloTarget = new HelloTarget();
        final String name = "woojung";
        assertEquals("Hello " + name, helloTarget.sayHello(name));
        assertEquals("Hi " + name, helloTarget.sayHi(name));
        assertEquals("ThankYou " + name, helloTarget.sayThankYou(name));
    }

    @Test
    public void dynamicProxyTest() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("say*");

        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(pointcut,
            new UppercaseAdvice());
        pfBean.addAdvisor(defaultPointcutAdvisor);
        Hello hello = (Hello) pfBean.getObject();
        final String name = "woojung";
        assertEquals(("hello " + name).toUpperCase(Locale.ROOT), hello.sayHello(name));
        assertEquals(("Hi " + name).toUpperCase(Locale.ROOT), hello.sayHi(name));
        assertEquals(("ThankYou " + name).toUpperCase(Locale.ROOT), hello.sayThankYou(name));
    }

    @Test
    public void dynamicProxyTestWithAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(pointcut,
            new UppercaseAdvice());
        pfBean.addAdvisor(defaultPointcutAdvisor);
        pfBean.setTarget(new HelloTarget());

        Hello hello = (Hello) pfBean.getObject();
        final String name = "woojung";
        assertEquals(("hello " + name).toUpperCase(Locale.ROOT), hello.sayHello(name));
        assertEquals(("Hi " + name).toUpperCase(Locale.ROOT), hello.sayHi(name));
        //
        assertEquals("ThankYou " + name, hello.sayThankYou(name));
    }

    @Test
    public void classNamePointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        pointcut.setMappedName("sayH*");

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(pointcut,
            new UppercaseAdvice());
        pfBean.addAdvisor(defaultPointcutAdvisor);
        pfBean.setTarget(new HelloTarget());

        Hello hello = (Hello) pfBean.getObject();
        final String name = "woojung";
        assertEquals(("hello " + name).toUpperCase(Locale.ROOT), hello.sayHello(name));
        assertEquals(("Hi " + name).toUpperCase(Locale.ROOT), hello.sayHi(name));
        //
        assertEquals("ThankYou " + name, hello.sayThankYou(name));
    }

    // org.aopalliance.intercept.MethodInterceptor
    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();
            return result.toUpperCase(Locale.ROOT);
        }
    }

}
