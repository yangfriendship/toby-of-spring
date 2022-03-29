package springbook.factorybean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@Getter
@Setter
public class MessageFactoryBean implements FactoryBean<Message> {

    private String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
