package org.myspringmvc.context;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.myspringmvc.stereotype.Controller;
import org.myspringmvc.web.bind.annotation.RequestMapping;
import org.myspringmvc.web.constant.Const;
import org.myspringmvc.web.method.HandlerMethod;
import org.myspringmvc.web.servlet.HandlerAdapter;
import org.myspringmvc.web.servlet.HandlerInterceptor;
import org.myspringmvc.web.servlet.HandlerMapping;
import org.myspringmvc.web.servlet.mvc.RequestMappingInfo;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private Map<String, Object> beanMap = new HashMap<>();

    // xmlPath 是子类 WebApplicationContext 传来的 springmvc.xml 的绝对路径
    public ApplicationContext(String xmlPath) throws Exception {
        // 解析 xml 文件
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(xmlPath));
        // 组件扫描，获取所有的 Controller 对象
        Element componentScanElement = (Element)document.selectSingleNode("/beans/component-scan");
        Map<RequestMappingInfo, HandlerMethod> map = componentScan(componentScanElement);
        System.out.println("Spring Web 容器当下状态：" + beanMap);

        // 创建视图解析器
        Element viewResolverElement = (Element) document.selectSingleNode("/beans/bean");
        createViewResolver(viewResolverElement);
        System.out.println("Spring Web 容器当下状态：" + beanMap);

        // 创建拦截器
        Element interceptorsElement = (Element) document.selectSingleNode("/beans/interceptors");
        createInterceptors(interceptorsElement);
        System.out.println("Spring Web 容器当下状态：" + beanMap);

        // 创建 org/myspringmvc/web/servlet/mvc/method/annotation 包下的所有 HandlerMapping
        createHandlerMapping(Const.DEFAULT_PACKAGE, map);

        // 创建 org/myspringmvc/web/servlet/mvc/method/annotation 包下的所有 HandlerAdapter
        createHandlerAdapter(Const.DEFAULT_PACKAGE);
        System.out.println("Spring Web 容器当下状态：" + beanMap);
    }

    private void createViewResolver(Element viewResolverElement) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 获取当前标签中的 class 属性
        String className = viewResolverElement.attributeValue(Const.BEAN_TAG_CLASS_ATTRIBUTE);
        System.out.println("视图解析器名：" + className);
        // 通过反射机制创建对象
        Class<?> clazz = Class.forName(className);
        // 创建视图解析器对象
        Object bean = clazz.newInstance();
        // 获取当前 bean 节点下的字节点 property 属性
        List<Element> property = viewResolverElement.elements(Const.PROPERTY_TAG_NAME);
        for (Element propertyElement : property) {
            // 获取 property 标签中的 name 属性
            String fieldName = propertyElement.attributeValue(Const.PROPERTY_NAME);
            // 将 fieldName 转换成 setter 方法对应的名字
            String setMethodName = fieldNameToSetMethodName(fieldName);
            // 获取 property 标签中的 value 属性
            String fieldValue = propertyElement.attributeValue(Const.PROPERTY_VALUE);
            System.out.println("property 标签的 name 属性：" + fieldName);
            System.out.println("setter 方法名：" + setMethodName);
            System.out.println("property 标签的 value 属性：" + fieldValue);
            // 通过 setMethodName 获取 setter 方法发
            Method setMethod = clazz.getDeclaredMethod(setMethodName, String.class);
            // 通过反射调用 setter 方法
            setMethod.invoke(bean, fieldValue);
        }
        // 将视图解析器对象添加到 IoC 容器
        beanMap.put(Const.VIEW_RESOLVER, bean);
    }

    // 将 fieldName 转换成 setter 方法对应的名字
    private String fieldNameToSetMethodName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private void createHandlerAdapter(String defaultPackage) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String defaultPath = defaultPackage.replace(".", "/");
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(defaultPath).getPath();
        System.out.println("HandlerAdapter 扫描路径：" + absolutePath);
        File file = new File(absolutePath);
        File[] files = file.listFiles();
        for (File f : files) {
            String classFileName = f.getName();
            System.out.println("classFileName = " + classFileName);
            String simpleClassName = classFileName.substring(0, classFileName.lastIndexOf("."));
            String className = defaultPackage + "." + simpleClassName;
            // 获取 Class
            Class<?> clazz = Class.forName(className);
            // 只用实现了 HandlerMapping 接口的才创建出对象
            if (HandlerAdapter.class.isAssignableFrom(clazz)) {
                Object bean = clazz.newInstance();
                beanMap.put(Const.HANDLER_ADAPTER, bean);
                // 只使用满足条件的第一个 HandlerAdapter 的实现类
                return;
            }
        }
    }

    private void createHandlerMapping(String defaultPackage, Map<RequestMappingInfo, HandlerMethod> map) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String defaultPath = defaultPackage.replace(".", "/");
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(defaultPath).getPath();
        System.out.println("HandlerMapping 扫描路径：" + absolutePath);
        File file = new File(absolutePath);
        File[] files = file.listFiles();
        for (File f : files) {
            String classFileName = f.getName();
            System.out.println("classFileName = " + classFileName);
            String simpleClassName = classFileName.substring(0, classFileName.lastIndexOf("."));
            String className = defaultPackage  + "." + simpleClassName;
            // 获取 Class
            Class<?> clazz = Class.forName(className);
            // 只用实现了 HandlerMapping 接口的才创建出对象
            if (HandlerMapping.class.isAssignableFrom(clazz)) {
                // 通过反射获取 HandlerMapping 的构造器
                Constructor<?> con = clazz.getDeclaredConstructor(Map.class);
                // 通过有参构造器创建对象，给 map 集合赋值
                Object bean = con.newInstance(map);
                beanMap.put(Const.HANDLER_MAPPING, bean);
                // 只使用满足条件的第一个 HandlerMapping 的实现类
                return;
            }
        }
    }

    // 创建拦截器
    private void createInterceptors(Element interceptorsElement) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 用一个 List 集合存储所有的拦截器对象
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        // 获取当前标签中的所有 bean 标签
        List<Element> beans = interceptorsElement.elements("bean");
        for (Element beanElement : beans) {
            // 获取当前标签中的 class 属性
            String className = beanElement.attributeValue(Const.BEAN_TAG_CLASS_ATTRIBUTE);
            // 创建对象
            Class<?> clazz = Class.forName(className);
            Object interceptor = clazz.newInstance();
            interceptors.add((HandlerInterceptor) interceptor);
        }
        // 存储到 IoC 容器中
        beanMap.put(Const.INTERCEPTORS, interceptors);
    }

    // 组件扫描
    private Map<RequestMappingInfo, HandlerMethod> componentScan(Element componentScanElement) throws Exception{
        // 创建请求信息的封装集合
        HashMap<RequestMappingInfo, HandlerMethod> map = new HashMap<>();

        // 获取包名
        String basePackage = componentScanElement.attributeValue(Const.BASE_PACKAGE);
        System.out.println("组件扫描的包：" + basePackage);
        // 通过包的路径获取该文件的绝对路径
        String dirPath = Thread.currentThread().getContextClassLoader().getResource(basePackage.replace(".", "/")).getPath();
        System.out.println("组件包对应的绝对路径：" + dirPath);
        File file = new File(dirPath);
        if(file.isDirectory()){
            // 获取该目录下的所有子文件
            File[] files = file.listFiles();
            for (File classFile : files){
                if(classFile.getName().endsWith(Const.SUFFIX_CLASS)){
                    String simpleName = classFile.getName().substring(0, classFile.getName().lastIndexOf("."));
                    String className = basePackage + "." + simpleName;
                    System.out.println("Controller 的完整类名：" + className);
                    // 如果类上有 @Controller 注解，则实例化 Controller 对象，并将其存储到 IoC 容器中
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        // 创建 Controller 对象
                        Object bean = clazz.newInstance();
                        // 存到 beanMap 集合中
                        beanMap.put(firstCharLowerCase(simpleName), bean);
                    }
                    Constructor<?> defaultCon = clazz.getDeclaredConstructor();
                    Object bean = defaultCon.newInstance();
                    beanMap.put(firstCharLowerCase(clazz.getSimpleName()), bean);

                    // 创建这个 bean 中的所有 HandlerMethod 对象并将其放到 map 集合中
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            // 获取方法上 @RequestMapping 注解的信息
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            // 创建 RequestMappingInfo 对象（key）
                            RequestMappingInfo requestMappingInfo = new RequestMappingInfo();
                            requestMappingInfo.setRequestURI(requestMapping.value()[0]); // 获取注解上的请求路径
                            requestMappingInfo.setMethod(requestMapping.method().toString()); // 获取注解上的请求方式
                            // 创建 HandlerMethod 对象（value）
                            HandlerMethod handlerMethod = new HandlerMethod();
                            handlerMethod.setHandler(bean); // 当前 Controller 对象
                            handlerMethod.setMethod(method); // 当前方法
                            map.put(requestMappingInfo, handlerMethod);

                        }
                    }
                }
            }
        }
        return map;
    }

    // 首字母转小写
    private String firstCharLowerCase(String simpleName) {
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    public Object getBean(String beanName){
        return beanMap.get(beanName);
    }
}
