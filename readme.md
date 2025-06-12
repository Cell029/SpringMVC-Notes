# 一. SpringMVC 概述

## 1. 定义

SpringMVC 是一个基于 Servlet 的 Web 框架，用于构建 Web 应用程序，基于 Spring 的 IOC、AOP 等核心技术，提供了灵活、高效、松耦合的 Web 开发方式。
MVC：Model（模型），处理业务逻辑，封装数据；View（视图），展示页面结果；Controller（控制器），接收请求、调度处理逻辑和视图

****
## 2. SpringMVC 的作用

### 1. 统一的入口控制机制

- SpringMVC：通过配置 DispatcherServlet 作为前端控制器，统一处理所有的 HTTP 请求，它根据请求路径自动分发到对应的处理方法（Controller），不需要手动写大量重复的路径判断逻辑。 
- Servlet 开发：需要手动编写 Servlet 类，并在 web.xml 中配置每一个请求路径与 Servlet 类的映射关系，路径管理复杂、扩展性差，维护成本高

### 2. 自动的数据绑定

- SpringMVC：可以自动将表单中的参数绑定到一个 JavaBean 对象上，只需在控制器方法中声明该对象即可，SpringMVC 会自动完成类型转换和赋值。
- Servlet 开发：需要手动通过 request.getParameter("...") 获取每个参数，并手动 set 到 JavaBean 中

### 3. IOC 容器管理对象

- SpringMVC：依赖 Spring 的 IoC 容器自动管理 Controller、Service、Repository 等对象的创建和生命周期，只需通过注解如 @Controller、@Service 和 @Autowired 注入即可使用
- Servlet 开发：需要手动创建所有对象实例，自己管理对象间的依赖关系，耦合度高

### 4. 统一的请求处理机制（拦截器 & 异常处理）

- SpringMVC： 
 1. 提供了强大的拦截器机制（AOP 编程：HandlerInterceptor），可对请求进行前置处理、后置处理和最终处理
 2. 提供异常处理器（@ExceptionHandler、ControllerAdvice），统一处理系统异常
- Servlet 开发：需要手动实现 Filter、Listener 等接口来自行编写拦截逻辑和异常捕获，代码复杂

### 5. 视图解析

- SpringMVC：
1. 支持视图解析器（如 InternalResourceViewResolver）自动将控制器返回的视图名映射为实际 JSP 或其他模板路径。
2. 支持多种视图技术：JSP、FreeMarker、Thymeleaf、PDF、Excel 等。

- servlet：需要手动调用 RequestDispatcher.forward() 或 response.sendRedirect() 来跳转页面，且通常只能使用 JSP，功能单一

****
## 3. 第一个 SpringMVC 程序

- 第一步：配置 [web.xml](./Demo1-First/src/main/webapp/WEB-INF/web.xml) 文件(在 WEB-INF 目录下)

SpringMVC 是一个 web 框架，在 javaweb 中 Servlet 负责接收请求，处理请求，以及响应，而 SpringMVC 框架中已经自动写好了一个 DispatcherServlet（前端控制器），它负责：

1. 接收客户端的HTTP请求：DispatcherServlet监听来自Web浏览器的HTTP请求，然后根据请求的URL将请求数据解析为Request对象 
2. 处理请求的URL：DispatcherServlet将请求的URL（Uniform Resource Locator）与处理程序进行匹配，确定要调用哪个控制器（Controller）来处理此请求
3. 调用相应的控制器：DispatcherServlet将请求发送给找到的控制器处理，控制器将执行业务逻辑，然后返回一个模型对象（Model）
4. 渲染视图：DispatcherServlet将调用视图引擎，将模型对象呈现为用户可以查看的HTML页面 
5. 返回响应给客户端：DispatcherServlet将为用户生成的响应发送回浏览器，响应可以包括表单、JSON、XML、HTML以及其它类型的数据

```text
【客户端浏览器】
        │
        ▼
发送HTTP请求（例如：/user/list）
        │
        ▼
【DispatcherServlet】（前端控制器）
        │
        ├─> 调用 HandlerMapping（处理器映射器）
        │         └─ 根据请求URL找到对应的Controller方法
        │
        ├─> 调用 HandlerAdapter（处理器适配器）
        │         └─ 执行目标 Controller 的方法
        │                   （可能包含业务逻辑处理 + 返回 ModelAndView）
        │
        ├─> 调用 ViewResolver（视图解析器）
        │         └─ 将视图名解析为实际视图文件（如 JSP）
        │
        ├─> 渲染视图（将模型数据绑定到视图中）
        │
        ▼
返回 HTTP 响应（HTML、JSON、XML 等）
        │
        ▼
【客户端浏览器显示结果】
```

- 第二步：配置 [springmvc-servlet.xml](./Demo1-First/src/main/webapp/WEB-INF/springmvc-servlet.xml) 文件

SpringMVC 框架有它自己的配置文件，该配置文件的名字默认为：`<servlet-name>-servlet.xml`(根据 web.xml 文件中配置的来决定)，默认存放的位置是 WEB-INF 目录下：

主要是要配置两项：

- 第一项：组件扫描，spring 扫描这个包中的类，将这个包中的类实例化并纳入 IoC 容器的管理
- 第二项：视图解析器，Controller 方法返回的逻辑视图名称解析成实际的视图对象并返回给 DispatcherServlet，最终由 DispatcherServlet 将该视图对象转化为响应结果，呈现给用户

注意：如果采用了其它视图，就要配置对应的视图解析器，例如：

1. JSP的视图解析器：InternalResourceViewResolver
2. FreeMarker视图解析器：FreeMarkerViewResolver
3. Velocity视图解析器：VelocityViewResolver

- 第三步：提供视图

在 WEB-INF 目录下新建 templates 目录（由 springmvc-servlet 文件中配置的前缀决定），在 templates 目录中新建html文件，例如：[first.html](./Demo1-First/src/main/webapp/WEB-INF/templates/first.html)

- 第四步：配置控制控制器

给 [Controller](./Demo1-First/src/main/java/com/cell/myFirstSpringMVC/controller/FirstController.java) 添加 @Controller 注解，在方法上添加 @RequestMapping(value="template文件名")，
DispatcherServlet 获取到请求路径后根据注解找到对应的方法，从方法中获取逻辑视图的名（例如：first），然后拼接前缀与后缀（springmvc-servlet文件中配置的）得到物理视图名称（例如：/WEB-INF/templates/first.html），然后让视图解析器解析这个文件




