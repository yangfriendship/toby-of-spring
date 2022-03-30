package springbook.learningtest.spring.pointcut;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class TargetTest {

    @Test
    public void test() throws NoSuchMethodException {
        Method minus = Target.class.getMethod("minus", int.class, int.class);
        System.out.println(minus);
    }

    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
            "execution(public int springbook.learningtest.spring.pointcut.Target.minus(int,int) "
                + "throws java.lang.RuntimeException)");

        assertTrue(pointcut.getClassFilter().matches(Target.class));

        Method minus = Target.class.getMethod("minus", int.class, int.class);
        assertTrue(pointcut.getMethodMatcher().matches(minus, null));

        Method plus = Target.class.getMethod("plus", int.class, int.class);
        assertFalse(pointcut.getMethodMatcher().matches(plus, null));
    }


    public void pointcutMatcher(String exp, boolean expected, Class<?> clazz, String methodName,
        Class<?> args) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(exp);

        boolean matches1 = pointcut.getClassFilter().matches(clazz);
        Method method = clazz.getMethod(methodName, args);
        boolean matches2 = pointcut.getMethodMatcher().matches(method, null);
        assertEquals(expected, (matches1 && matches2));
    }

}