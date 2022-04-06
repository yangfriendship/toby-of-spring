package springbook.learningtest.spring.ioc.bean.objectfactory;

import javax.annotation.Resource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectService {

    @Resource
    private ObjectFactory<ClientObject> clientObjectObjectFactory;

    public ClientObject getObject() {
        return this.clientObjectObjectFactory.getObject();
    }

}
