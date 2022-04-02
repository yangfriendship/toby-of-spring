package springbook.learningtest.spring.ioc;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hello {

    private String name;

    private Printer printer;

    public String sayHello() {
        return "Hello" + this.name;
    }

    public void print() {
        this.printer.print(this.sayHello());
    }

}
