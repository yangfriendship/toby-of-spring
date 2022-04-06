package springbook.learningtest.spring.ioc.bean.scanner;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackageClasses = ServiceMarker.class,
    excludeFilters = {
        // @Configuration 이 붙은 모든 class 를 스캔 범위에서 제외
        @Filter(Configuration.class),
        // value 타입의 클레스를 제외한다.
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfig.class)
    }
)
@Import(DataConfig.class)
@ImportResource(value = "classpath:scannerContext.xml")
public class AppConfig {

}
