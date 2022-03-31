package springbook.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;

public class UppercaseHandler implements InvocationHandler {

    private final Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getReturnType() != String.class || !method.getName().startsWith("say")) {
            return method.invoke(target, args);
        }
        String result = (String) method.invoke(target, args);
        return result.toUpperCase(Locale.ROOT);
    }
}
