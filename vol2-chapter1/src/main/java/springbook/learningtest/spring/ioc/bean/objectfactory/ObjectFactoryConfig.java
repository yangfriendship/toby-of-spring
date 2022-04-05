package springbook.learningtest.spring.ioc.bean.objectfactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackageClasses = ObjectFactoryConfig.class)
public class ObjectFactoryConfig {

    @Bean
    public ObjectFactory<ClientObject> clientObjectObjectFactory() {
        ObjectFactory<ClientObject> aNew = () -> {
            ClientObject result = new ClientObject();
            result.setObject(dependencyObject());
            return result;
        };
        return aNew;
    }

    @Scope(value = "prototype")
    @Bean
    public ClientObject clientObjectWithBean() {
        ClientObject clientObject = new ClientObject();
        clientObject.setObject(dependencyObject());
        return clientObject;
    }

    @Bean
    public DependencyObject dependencyObject() {
        return new DependencyObject();
    }

    @Bean
    public FactoryTarget factoryTarget() {
        return new FactoryTarget();
    }

    @Bean
    public ObjectFactoryCreatingFactoryBean factoryTargetFactory() {
        ObjectFactoryCreatingFactoryBean objectFactoryCreatingFactoryBean = new ObjectFactoryCreatingFactoryBean();
        objectFactoryCreatingFactoryBean.setTargetBeanName("clientObjectWithBean"); // getBean 으로 가져올 빈의 이름을 등록
        return objectFactoryCreatingFactoryBean;
    }

    @Bean
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(ObjectServiceFactory.class);
        return factoryBean;
    }

}
