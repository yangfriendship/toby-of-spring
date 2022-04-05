package springbook.learningtest.spring.ioc.profileproperty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class ProfileConfig {

    @Bean
    public ApplicationContextInitializer applicationContextInitializer(
        AnnotationConfigApplicationContext ctx) {
        ApplicationContextInitializerImpl initializer = new ApplicationContextInitializerImpl();
        initializer.initialize(ctx);
        return initializer;
    }

    @Configuration
    @Profile("local")
    @PropertySource("/local.properties")
    static class LocalConfig {

        @Bean(name = "sayProfile")
        public SayCurrentProfile localProfile(@Value("current") String current) {
            LocalProfile profile = new LocalProfile();
            profile.setCurrent(current);
            return profile;
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//            configurer.setFileEncoding("UTF-8");
//            configurer.setLocation(new ClassPathResource("local.properties"));
            return configurer;
        }
    }

    @Profile("product")
    @Configuration
    @PropertySource("/product.properties")
    static class ProductConfig {

        @Bean(name = "sayProfile")
        public SayCurrentProfile productProfile(@Value("current") String current) {
            ProductProfile profile = new ProductProfile();
            profile.setCurrent(current);
            return profile;
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//            configurer.setFileEncoding("UTF-8");
//            configurer.setLocation(new ClassPathResource("product.properties"));
            return configurer;
        }
    }

}
