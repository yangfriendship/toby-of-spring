package springbook.learningtest.spring.ioc.bean.objectfactory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ObjectFactoryConfigTest {

    /*
     * 직접 ObjectFactory 를 빈으로 등록
     * */
    @Test
    public void objectFactoryTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ObjectFactoryConfig.class);
        ObjectFactory factory = ctx.getBean("clientObjectObjectFactory",
            ObjectFactory.class);
        ClientObject object1 = (ClientObject) factory.getObject();
        ClientObject object2 = (ClientObject) factory.getObject();

        assertNotSame(object1, object2);
        assertNotNull(object1.getObject());
        assertNotNull(object2.getObject());
        assertSame(object1.getObject(), object2.getObject());
    }

    /*
     * ObjectFactoryCreatingFactoryBean 를 구현하여 ObjectFactory 를 자동 생성
     * */
    @Test
    public void objectFactoryCreatingFactoryBeanTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ObjectFactoryConfig.class);
        ObjectFactory<ClientObject> objectFactory = ctx.getBean("factoryTargetFactory",
            ObjectFactory.class);   // factoryTargetFactory 는 objectFactoryCreatingFactoryBean 의 이름이다.

        ClientObject object1 = objectFactory.getObject();
        ClientObject object2 = objectFactory.getObject();
        assertNotSame(object1, object2);
        assertNotNull(object1.getObject());
        assertNotNull(object2.getObject());
        assertSame(object1.getObject(), object2.getObject());
    }

    /*
     * ServiceLocatorFactoryBean 를 빈으로 등록하여 빈을 생성하는 Interface 의 구현체를 주입받는 방식
     * */
    @Test
    public void serviceLocatorFactoryBean() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ObjectFactoryConfig.class);
        ObjectServiceFactory serviceFactory = ctx.getBean(ObjectServiceFactory.class);
        ClientObject object1 = serviceFactory.getClientObject();
        ClientObject object2 = serviceFactory.getClientObject();
        assertNotSame(object1, object2);
        assertSame(object1.getObject(), object2.getObject());
    }

    /*
     * 특정 Object 에게 ProtoType Bean 의 생성을 맡기는 방식도 사용할 수 있을거 같다. 아님말고
     * */
    @Test
    public void serviceTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ObjectFactoryConfig.class);
        ObjectService service = ctx.getBean(ObjectService.class);
        ClientObject object1 = service.getObject();
        ClientObject object2 = service.getObject();
        assertNotSame(object1, object2);
        assertSame(object1.getObject(), object2.getObject());
    }

}