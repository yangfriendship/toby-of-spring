package springbook.learningtest.spring.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public abstract class SimpleController implements Controller {

    private String[] requiredParams;
    private String viewName;

    public void setRequiredParams(String[] requiredParams) {
        this.requiredParams = requiredParams;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    final public ModelAndView handleRequest(HttpServletRequest req,
        HttpServletResponse res) throws Exception {
        // ViewName 이 없다면 예외 발생
        if (viewName == null) {
            throw new IllegalStateException();
        }

        // 필요한 parameter 를 추출하여 map 에 담는다.
        Map<String, String> params = new HashMap<>();
        for (String param : requiredParams) {
            String value = req.getParameter(param);
            if (value == null) {
                throw new IllegalStateException();
            }
            params.put(param, value);
        }

        Map<String, Object> model = new HashMap<>();
        // 서브 클래스에서 정의한 control() 메소드를 실행한다.
        this.control(params, model);

        return new ModelAndView(this.viewName, model);
    }

    public abstract void control(Map<String, String> params, Map<String, Object> model)
        throws Exception;
}
