package org.myspringmvc.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myspringmvc.ui.ModelMap;
import org.myspringmvc.web.method.HandlerMethod;
import org.myspringmvc.web.servlet.HandlerAdapter;
import org.myspringmvc.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取 Controller 对象
        Object controller = handlerMethod.getHandler();
        // 获取调用的方法
        Method method = handlerMethod.getMethod();
        // 通过反射机制调用方法
        ModelMap modelMap = new ModelMap();
        String viewName = (String) method.invoke(controller, modelMap); // 实际执行了 controller 的业务逻辑，所以获取到了它的返回值

        // 封装 ModelAndView 对象
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        modelAndView.setModel(modelMap);
        return modelAndView;
    }
}
