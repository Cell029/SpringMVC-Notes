package org.myspringmvc.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletRequest;
import org.myspringmvc.context.WebApplicationContext;
import org.myspringmvc.web.constant.Const;
import org.myspringmvc.web.method.HandlerMethod;
import org.myspringmvc.web.servlet.HandlerExecutionChain;
import org.myspringmvc.web.servlet.HandlerInterceptor;
import org.myspringmvc.web.servlet.HandlerMapping;
import org.myspringmvc.web.servlet.mvc.RequestMappingInfo;
import java.util.List;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

    // 处理器映射器主要就是通过以下 map 集合进行映射的
    // key 是请求信息，value 是该请求的请求方法
    private Map<RequestMappingInfo, HandlerMethod> map;

    // 在创建 HandlerMapping 对象时给 map 集合赋值
    public RequestMappingHandlerMapping(Map<RequestMappingInfo, HandlerMethod> map) {
        this.map = map;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        // 通过 request 对象获取请求路径，请求方式，将它们封装成 RequestMappingInfo 对象
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(request.getServletPath(), request.getMethod());

        // 创建处理器执行链对象
        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();

        // 通过 request 获取到请求信息，然后从 map 集合中获取对应的 HandlerMethod
        // 给执行链设置 HandlerMethod 对象
        handlerExecutionChain.setHandler(map.get(requestMappingInfo));

        // 获取所有拦截器
        WebApplicationContext webApplicationContext = (WebApplicationContext) request.getServletContext().getAttribute(Const.WEB_APPLICATION_CONTEXT);
        // 给执行链设置拦截器
        List<HandlerInterceptor> interceptors = (List<HandlerInterceptor>) webApplicationContext.getBean(Const.INTERCEPTORS);
        handlerExecutionChain.setInterceptorList(interceptors);

        return handlerExecutionChain;
    }
}
