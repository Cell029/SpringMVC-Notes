package org.myspringmvc.web.constant;

public class Const {
    // web.xml 文件中配置 DispatcherServlet 的初始化参数的 contextConfigLocation 的名字
    public static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
    // contextConfigLocation 的前缀
    public static final String PREFIX_CLASSPATH = "classpath:";

    public static final String WEB_APPLICATION_CONTEXT = "webApplicationContext";
    // HandlerMapping 和 HandlerAcceptor 的实现类都在这个包下
    public static final String DEFAULT_PACKAGE = "org.myspringmvc.web.servlet.mvc.method.annotation";
    // springmvc.xml 文件中的 component-scan 标签的 base-package 属性名
    public static final String BASE_PACKAGE = "base-package";
    // 以 .class 结尾
    public static final String SUFFIX_CLASS= ".class";
    // bean 标签的 class 属性
    public static final String BEAN_TAG_CLASS_ATTRIBUTE = "class";
    // property 标签
    public static final String PROPERTY_TAG_NAME = "property";

    public static final String PROPERTY_NAME = "name";

    public static final String PROPERTY_VALUE = "value";
    // 视图解析器名
    public static final String VIEW_RESOLVER = "viewResolver";

    public static final String INTERCEPTORS = "interceptors";

    public static final String HANDLER_MAPPING = "handlerMapping";

    public static final String HANDLER_ADAPTER = "handlerAdapter";
}
