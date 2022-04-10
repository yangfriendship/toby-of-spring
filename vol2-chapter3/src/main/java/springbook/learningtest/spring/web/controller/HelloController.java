package springbook.learningtest.spring.web.controller;

import java.util.Map;
import springbook.learningtest.spring.web.annotation.RequiredParams;
import springbook.learningtest.spring.web.annotation.ViewName;

public class HelloController extends SimpleController {

    public HelloController() {
        this.setViewName("/views/hello.jsp");
        this.setRequiredParams(new String[]{"name"});
    }

    @ViewName("hello")
    @RequiredParams(value = {"name"})
    @Override
    public void control(Map<String, String> params, Map<String, Object> models) {
        models.put("message", "Hello " + params.get("name"));
    }
}
