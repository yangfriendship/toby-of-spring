package springbook.learningtest.spring.ioc.profileproperty;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public class ProfileConfigTest {

    @Test
    public void profileTest_local() {
        // create application context
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("local");
        ctx.register(ProfileConfig.class);
        ctx.refresh();

        // Dependency loop up test bean
        SayCurrentProfile sayProfile = ctx.getBean("sayProfile", SayCurrentProfile.class);

        //
        assertNotNull(sayProfile);
        assertTrue(sayProfile instanceof LocalProfile,
            "profile == local then return LocalProfile Type Bean");
        assertFalse(sayProfile instanceof ProductProfile);
        assertEquals("local", sayProfile.sayProfile());
    }

    @Test
    public void profileTest_product() {
        // create application context
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("product");
        ctx.register(ProfileConfig.class);
        ctx.refresh();

        // Dependency loop up test bean
        SayCurrentProfile sayProfile = ctx.getBean("sayProfile", SayCurrentProfile.class);

        //
        assertNotNull(sayProfile);
        assertTrue(sayProfile instanceof ProductProfile,
            "profile == product then return ProductProfile Type Bean");
        assertFalse(sayProfile instanceof LocalProfile);
        assertEquals("product", sayProfile.sayProfile());
    }

    @Test
    public void ApplicationContextInitializerImplTest() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("product");
        ctx.register(ProfileConfig.class);
        ctx.refresh();

        Environment env = ctx.getEnvironment();
        String property = env.getProperty("props.name");
        assertEquals("woojung", property);
    }

}