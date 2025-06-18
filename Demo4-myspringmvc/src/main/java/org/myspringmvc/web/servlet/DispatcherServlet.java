package org.myspringmvc.web.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myspringmvc.context.WebApplicationContext;
import org.myspringmvc.web.constant.Const;

import java.io.IOException;
import java.util.Locale;


public class DispatcherServlet extends HttpServlet {

    // 视图解析器
    private ViewResolver viewResolver;

    // 处理器映射器
    private HandlerMapping handlerMapping;

    // 处理器适配器
    private HandlerAdapter handlerAdapter;

    // DispatcherServlet 被初始化后会调用 init(ServletConfig)
    // 所以会从父类中找，最终执行 HttpServlet 的 init(ServletConfig)
    // 最终在 HttpServletBean 的 init(ServletConfig) 中调用 DispatcherServlet 的 init() 方法
    @Override
    public void init() throws ServletException {
        /*根据 web 文件中的信息找到 springmvc.xml 文件
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        */
        // 因为 web 容器初始化时会将 web.xml 文件中的信息封装进 ServletConfig 对象中
        ServletConfig servletConfig = this.getServletConfig();
        String contextConfigLocation = servletConfig.getInitParameter(Const.CONTEXT_CONFIG_LOCATION);
        System.out.println("contextConfigLocation --> " + contextConfigLocation);
        // 判断是否以 classpath: 开头
        String springMvcConfigPath = null;
        if (contextConfigLocation.trim().startsWith(Const.PREFIX_CLASSPATH)) {
            // 条件成立表示这个配置文件要从类的根路径开始找
            springMvcConfigPath = Thread.currentThread().getContextClassLoader().getResource(contextConfigLocation.substring(Const.PREFIX_CLASSPATH.length())).getPath();
            System.out.println("springMvcConfigPath 的绝对路径 --> " + springMvcConfigPath);
        }

        // 初始化 Spring Web 容器，将所有应该创建的对象全部创建出来交给 IoC 容器管理
        WebApplicationContext webApplicationContext = null;
        try {
            webApplicationContext = new WebApplicationContext(springMvcConfigPath, this.getServletContext());
            // webApplicationContext 代表的就是 Spring Web 容器，最好把它存储到 ServletContext 中，以便后期使用
            this.getServletContext().setAttribute(Const.WEB_APPLICATION_CONTEXT, webApplicationContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 初始化处理器映射器
        this.handlerMapping = (HandlerMapping) webApplicationContext.getBean(Const.HANDLER_MAPPING);

        // 初始化处理器适配器
        this.handlerAdapter  = (HandlerAdapter) webApplicationContext.getBean(Const.HANDLER_ADAPTER);

        // 初始化视图解析器
        this.viewResolver = (ViewResolver) webApplicationContext.getBean(Const.VIEW_RESOLVER);

    }

    // 只要浏览器发送一次请求，就会调用一次 service 方法
    // 实际上每次请求时先调用 HttpServlet 的 service(ServletRequest req, ServletResponse res)，但这个方法中对 req 和 res 进行了转型
    // 将它们转换成 HttpServletRequest 和 HttpServletResponse
    // 然后传递给子类重写的 service 方法
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    // 处理请求的核心方法
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // 1. 根据请求对象获取对应的处理器执行链对象
            // 通过前端提交的请求（包括请求路径和请求方式），来映射底层要执行的 HandlerMethod
            HandlerExecutionChain mappedHandler = handlerMapping.getHandler(request);

            // 2. 根据处理器方法获取对应的处理器适配器对象
            HandlerAdapter ha = this.handlerAdapter;

            // 3. 执行拦截器的 preHandle 方法
            if (!mappedHandler.applyPreHandle(request, response)) {
                return;
            }

            // 4. 执行处理器方法并返回 ModelAndView
            ModelAndView mv = ha.handle(request, response, mappedHandler.getHandler());

            // 5. 执行拦截器的 postHandle 方法
            mappedHandler.applyPostHandle(request, response, mv);

            // 6. 响应给浏览器
            // 获取视图对象
            View view = viewResolver.resolveViewName(mv.getView().toString(), Locale.CHINA);
            // 渲染
            view.render(mv.getModel(), request, response);

            // 7. 执行拦截器的 afterCompletion 方法
            mappedHandler.triggerAfterCompletion(request, response, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
