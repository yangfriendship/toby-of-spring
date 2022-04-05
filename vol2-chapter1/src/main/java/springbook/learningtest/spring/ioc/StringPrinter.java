package springbook.learningtest.spring.ioc;

public class StringPrinter implements Printer {

    private final StringBuffer stringBuffer = new StringBuffer();

    @Override
    public void print(String sayHello) {
        this.stringBuffer.append(sayHello);
    }

    @Override
    public String toString() {
        return this.stringBuffer.toString();
    }
}
