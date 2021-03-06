package springbook.learningtest.jdk.proxy;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    public void setMappedClassName(String mappedClassName) {
        this.setClassFilter(new SimpleClassFilter(mappedClassName));
    }

    static class SimpleClassFilter implements ClassFilter {

        private final String mappedName;

        SimpleClassFilter(String mappedName) {
            this.mappedName = mappedName;
        }

        @Override
        public boolean matches(Class<?> clazz) {
//            boolean b = PatternMatchUtils.simpleMatch(clazz.getSimpleName(), this.mappedName);
            boolean b = PatternMatchUtils.simpleMatch(this.mappedName, clazz.getSimpleName());
            return b;
//            return b;
        }
    }

}
