package springbook.learningtest.spring.web.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"
    ,"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
})
public class HelloControllerTest {

        @Test
    public void helloControllerTest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "woojung");
        Map<String, Object> models = new HashMap<>();

        new HelloController().control(params, models);

        assertEquals("Hello woojung", models.get("message"));

    }

    @Test
    public void adaptorTest() {

    }

}