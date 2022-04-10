package springbook.learningtest.spring.web.handleradaptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import springbook.learningtest.spring.web.annotation.RequiredParams;
import springbook.learningtest.spring.web.annotation.ViewName;
import springbook.learningtest.spring.web.controller.SimpleController;

public class SimpleHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof SimpleController);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        final Method method = ReflectionUtils.findMethod(handler.getClass(), "control", Map.class,
            Map.class);

        ViewName viewName = AnnotationUtils.getAnnotation(method, ViewName.class);
        RequiredParams requiredParams = AnnotationUtils.getAnnotation(method, RequiredParams.class);
        final Map<String, String> params = new HashMap<>();

        final String[] values = requiredParams.value();
        for (final String param : values) {
            String value = request.getParameter(param);
            if (!StringUtils.hasText(value)) {
                throw new IllegalArgumentException();
            }
            params.put(param, value);
        }
        Map<String, Object> model = new HashMap<>();
        ((SimpleController) handler).control(params, model);

        return new ModelAndView(viewName.value(), model);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1;
    }
}
