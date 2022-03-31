package springbook.factorybean;

import java.lang.reflect.Proxy;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.handler.TransactionHandler;

@Setter
public class TxProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private String pattern;
    private PlatformTransactionManager transactionManager;
    private Class<?> serviceInterface;

    @Override
    public Object getObject() throws Exception {
        TransactionHandler handler = new TransactionHandler();
        handler.setTarget(target);
        handler.setTransactionManager(transactionManager);
        handler.setPattern(pattern);

        return Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[]{serviceInterface},
            handler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
