package org.myspringmvc.web.servlet;

import java.util.Locale;

public interface ViewResolver {
    // 解析逻辑视图名称，返回视图对象
    View resolveViewName(String viewName, Locale locale) throws Exception;
}
