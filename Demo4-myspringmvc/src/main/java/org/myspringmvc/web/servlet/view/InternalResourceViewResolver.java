package org.myspringmvc.web.servlet.view;

import org.myspringmvc.web.servlet.View;
import org.myspringmvc.web.servlet.ViewResolver;

import java.util.Locale;

// InternalResourceViewResolver 是视图解析器，它的职责是创建并返回一个 InternalResourceView 实例（即真正执行渲染的视图对象）
// 根据控制器返回的逻辑视图名，拼接路径前缀/后缀，创建视图对象
public class InternalResourceViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return null;
    }
}
