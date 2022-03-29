package springbook.factorybean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/FactoryBeanTest-context.xml"})
public class FactoryBeanTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    public void getMessageFromFactoryBean() {
        Message message = ctx.getBean("message", Message.class);
        Message message2 = ctx.getBean("message", Message.class);

        String text = message.getText();
        assertEquals("Factory Bean", text);
        assertNotSame(message, message2);

        MessageFactoryBean factoryBean = ctx.getBean("&message", MessageFactoryBean.class);
    }

}