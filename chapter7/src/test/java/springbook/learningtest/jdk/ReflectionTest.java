package springbook.learningtest.jdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;
import org.junit.Test;

public class ReflectionTest {

    @Test
    public void invokeMethod() throws Exception {
        final String name = "spring";
        assertEquals(6, name.length());

        final Method length = String.class.getMethod("length");
        assertEquals(6, (Integer) length.invoke(name));

        final Method charAt = String.class.getMethod("charAt", int.class);
        final char charAtResult = (Character) charAt.invoke(name, 0);
        assertEquals(name.charAt(0), charAtResult);
    }

    @Test
    public void helloUppercaseTest() {
        Hello helloTarget = new HelloUppercase(new HelloTarget());
        final String name = "woojung";
        assertEquals(("Hello " + name).toUpperCase(Locale.ROOT), helloTarget.sayHello(name));
        assertEquals(("Hi " + name).toUpperCase(Locale.ROOT), helloTarget.sayHi(name));
        assertEquals(("ThankYou " + name).toUpperCase(Locale.ROOT), helloTarget.sayThankYou(name));
    }

    @Test
    public void invocationHandlerTest() {
        Hello hello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[]{Hello.class},
            new UppercaseHandler(new HelloTarget())
        );
        final String name = "woojung";
        assertEquals(("Hello " + name).toUpperCase(Locale.ROOT), hello.sayHello(name));
        assertEquals(("Hi " + name).toUpperCase(Locale.ROOT), hello.sayHi(name));
        assertEquals(("ThankYou " + name).toUpperCase(Locale.ROOT), hello.sayThankYou(name));
    }



}
