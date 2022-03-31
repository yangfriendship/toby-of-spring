package springbook.learningtest.jdk;

import java.util.Locale;

public class HelloUppercase implements Hello {

    private final Hello hello;

    public HelloUppercase(Hello hello) {
        this.hello = hello;
    }

    @Override
    public String sayHello(String name) {
        return this.hello.sayHello(name).toUpperCase(Locale.ROOT);
    }

    @Override
    public String sayHi(String name) {
        return this.hello.sayHi(name).toUpperCase(Locale.ROOT);
    }

    @Override
    public String sayThankYou(String name) {
        return this.hello.sayThankYou(name).toUpperCase(Locale.ROOT);
    }
}
