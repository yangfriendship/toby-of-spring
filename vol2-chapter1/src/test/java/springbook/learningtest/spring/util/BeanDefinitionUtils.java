package springbook.learningtest.spring.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

public class BeanDefinitionUtils {

    public static <T extends BeanDefinitionRegistry & ApplicationContext>
    void printBeanDefinitionsForAnnotation(T input) {
        List<List<String>> roleBeanInfos = new ArrayList<>();
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());

        for (String name : input.getBeanDefinitionNames()) {
            int role = input.getBeanDefinition(name).getRole();
            List<String> beanInfo = roleBeanInfos.get(role);
            beanInfo.add(role + "\t" + name + "\t" + input.getBean(name).getClass().getName());
        }

        for (List<String> beanInfos : roleBeanInfos) {
            for (String beanInfo : beanInfos) {
                System.out.println(beanInfo);
            }
        }
    }

}
