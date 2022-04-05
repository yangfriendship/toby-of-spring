package springbook.learningtest.spring.ioc.bean.example;

import javax.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.Printer;

@Component
@Getter
@Setter
public class ResourceHello {

    @Resource
    private Printer stringPrinter;
    @Resource(name = "consolePrinter")
    private Printer printer;

    public Printer getConsolePrinter() {
        return this.printer;
    }

}
