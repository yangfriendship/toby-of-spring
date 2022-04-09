package springbook.temp.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import springbook.temp.HelloSpring;

public class HelloController implements Controller {

    @Autowired
    private HelloSpring helloSpring;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        final String name = request.getParameter("name");
        final String message = this.helloSpring.sayHello(name);
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        return new ModelAndView("/views/hello.jsp", map);
    }

}