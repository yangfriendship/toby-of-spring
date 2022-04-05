package springbook.learningtest.spring.ioc;

import org.springframework.util.StringUtils;

public class ConsolePrinter implements Printer {

    @Override
    public void print(String sayHello) {
        if (!StringUtils.hasText(sayHello)){
            throw new IllegalArgumentException("문자열을 입력하세요.");
        }
        System.out.println(sayHello);
    }
}
