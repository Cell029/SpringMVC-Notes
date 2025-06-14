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

**如果在 web.xml 文件中没有指定 init-param，它会自动去加载一个默认配置文件（如上面的配置），即默认找 /WEB-INF/springmvc-servlet.xml 配置文件**

- 第三步：提供视图

在 WEB-INF 目录下新建 templates 目录（由 springmvc-servlet 文件中配置的前缀决定），在 templates 目录中新建html文件，例如：[first.html](./Demo1-First/src/main/webapp/WEB-INF/templates/first.html)

- 第四步：配置控制控制器

给 [Controller](./Demo1-First/src/main/java/com/cell/myFirstSpringMVC/controller/FirstController.java) 添加 @Controller 注解，在方法上添加 @RequestMapping(value="template文件名")，
DispatcherServlet 获取到请求路径后根据注解找到对应的方法，从方法中获取逻辑视图的名（例如：first），然后拼接前缀与后缀（springmvc-servlet文件中配置的）得到物理视图名称（例如：/WEB-INF/templates/first.html），然后让视图解析器解析这个文件

****
# 二. @RequestMapping

`@RequestMapping` 注解是 Spring MVC 框架中的一个控制器映射注解，用于将请求映射到相应的处理方法上。具体来说，它可以将指定 URL 的请求绑定到一个特定的方法或类上（只能在方法或类上），从而实现对请求的处理和响应

## 1. 类与方法上的结合使用

在同一个 webapp 中，RequestMapping 必须具有唯一性，如果相同则会报错，有两种解决方案：

- 第一种：将方法上 RequestMapping 的映射路径修改为不一样的

```java
@RequestMapping("/user/detail")
public String toDetail(){
    return "/user/detail";
}

@RequestMapping("/product/detail")
public String toDetail(){
    return "/product/detail";
}
```

- 第二种：在类上和方法上都使用 RequestMapping 注解来进行路径的映射，类上映射的就是所有方法的前缀路径，例如在类上映射的路径是"/a"，在方法上映射的路径是"/b"，那么整体表示映射的路径就是："/a/b"

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/detail")
    public String toDetail(){
        return "/user/detail";
    }
}

@Controller
@RequestMapping("/product")
public class ProductController {
    @RequestMapping("/detail")
    public String toDetail(){
        return "/product/detail";
    }
}
```

****
## 2. @RequestMapping 的 value 属性

基本作用：value 属性用于指定当前方法或类所处理的请求路径 URL

### 2.1 提供多个路径

value 是一个 `String[]` 类型，所以它可以填写多个请求路径

```java
@Controller
public class LoginController {
    // 用户访问 /login 或 /signin 时，都会进入 loginPage() 方法
    @RequestMapping({"/login", "/signin"})
    public String loginPage() {
        System.out.println("登录页面访问");
        return "login";
    }
}
```

****
### 2.2 Ant 风格的 value

value 属性除了可以直接写 URL 路径，还支持 Ant 风格路径匹配（模糊匹配），用于匹配复杂或不确定的请求路径，通配符包括：

- ?：代表任意一个字符
- *：代表0到N个任意字符
- **：代表0到N个任意字符，并且路径中可以出现路径分隔符 /

1、`?` 匹配单个字符

```java
@RequestMapping("/user?") // 可以匹配 /user1、/userA、/userX 等，不能匹配 /user12，每一个 ? 匹配一个字符，只能匹配一个字符的位置
@RequestMapping("/us?er") // 可以匹配 /us1er、/usAer....
```

2、 `*` 匹配任意字符（不含 /）

```java
@RequestMapping("/user/*") // 匹配 /user/abc、/user/123，但不匹配 /user/abc/def
@RequestMapping("/us*er") // 匹配 /usabcer、/us123er
```

3、`**` 匹配任意层路径（包括 /）

`**` 必须作为路径的独立一段使用，左右不能跟其它字符拼接使用

```java
@RequestMapping("/user/**") // 匹配 /user/abc、/user/abc/def、/user/a/b/c
@RequestMapping("/**/end") // 匹配以 /end 结尾的多层路径
@RequestMapping("/**") // 匹配所有请求路径（可作为拦截器或异常处理路径）

@RequestMapping("/abc**def") // 错误，不能这样使用
```

****
### 2.3 value 中的占位符

到目前为止，常用的请求路径是这样的格式：uri?name1=value1&name2=value2&name3=value3，
除了这种方式，还有另外一种格式的请求路径，格式为：uri/value1/value2/value3，这样的请求路径叫做 RESTful 风格的请求路径，例如：

```text
普通的请求路径：http://localhost:8080/springmvc/login?username=admin&password=123&age=20
RESTful 风格的请求路径：http://localhost:8080/springmvc/login/admin/123/20
```

想要使用这种方式可以在 value 属性中使用占位符（@PathVariable("xxx")， xxx 就是占位符名称，必须和 {} 中保持一致）

```java
@RequestMapping("/product/{id}")
public String show(@PathVariable String id) { ... } // 参数名与占位符名称相同，可以省略注解中的 "xxx"

@RequestMapping(value="/testRESTful/{id}/{username}/{age}")
public String testRESTful(
        @PathVariable("id")
        int id,
        @PathVariable("username")
        String username,
        @PathVariable("age")
        int age){
    System.out.println(id + "," + username + "," + age);
    return "testRESTful";
}
```

注意：

- @PathVariable 只能用于匹配 value 中 {} 的部分，不能匹配 * 或 ?
- 占位符不能与 Ant 风格混用（如 /user/{id}*.html 是非法的）
- {} 中不能使用正则表达式限制（SpringMVC 不支持，但 SpringBoot 中可通过正则匹配）

****
### 2.4 method 属性

method 属性用于限定 HTTP 请求的方式，比如 GET、POST、PUT、DELETE 等，根据不同的请求方式，即使路径相同也会进入不同的方法：

```java
@RequestMapping(value = "/hello", method = RequestMethod.GET)
public String helloGet() {
    return "helloGet";
}

@RequestMapping(value = "/hello", method = RequestMethod.POST)
public String helloPost() {
    return "helloPost";
}
```

可以使用数组的形式指定多个请求方式：

```java
@RequestMapping(value = "/multi", method = {RequestMethod.GET, RequestMethod.POST})
public String handleBoth() {
    return "handleBoth";
}
```

也可也使用注解的方式简化：

| 注解               | 等价方式                               |
| ---------------- | ---------------------------------- |
| `@GetMapping`    | `@RequestMapping(method = GET)`    |
| `@PostMapping`   | `@RequestMapping(method = POST)`   |
| `@PutMapping`    | `@RequestMapping(method = PUT)`    |
| `@DeleteMapping` | `@RequestMapping(method = DELETE)` |
| `@PatchMapping`  | `@RequestMapping(method = PATCH)`  |


```java
@GetMapping("/info")
public String getInfo() {
    return "info";
}

@PostMapping("/submit")
public String submit() {
    return "submitted";
}
```

****
### 2.5 web 的请求方式

- **GET：获取资源，只允许读取数据，不影响数据的状态和功能。使用 URL 中传递参数或者在 HTTP 请求的头部使用参数，服务器返回请求的资源。**
- **POST：向服务器提交资源，可能还会改变数据的状态和功能。通过表单等方式提交请求体，服务器接收请求体后，进行数据处理。**
- **PUT：更新资源，用于更新指定的资源上所有可编辑内容。通过请求体发送需要被更新的全部内容，服务器接收数据后，将被更新的资源进行替换或修改。**
- **DELETE：删除资源，用于删除指定的资源。将要被删除的资源标识符放在 URL 中或请求体中。**
- **HEAD：请求服务器返回资源的头部，与 GET 命令类似，但是所有返回的信息都是头部信息，不能包含数据体。主要用于资源检测和缓存控制。**
- PATCH：部分更改请求。当被请求的资源是可被更改的资源时，请求服务器对该资源进行部分更新，即每次更新一部分。
- OPTIONS：请求获得服务器支持的请求方法类型，以及支持的请求头标志。“OPTIONS *”则返回支持全部方法类型的服务器标志。
- TRACE：服务器响应输出客户端的 HTTP 请求，主要用于调试和测试。
- CONNECT：建立网络连接，通常用于加密 SSL/TLS 连接。

注意：

1. 使用超链接以及原生的form表单只能提交get和post请求
2. 使用超链接发送的是get请求
3. 使用form表单，如果没有设置method，发送get请求
4. 使用form表单，设置method="get"，发送get请求
5. 使用form表单，设置method="post"，发送post请求
6. **使用form表单，设置method="put/delete/head"，发送get请求**

****
### 2.6 params 属性

params 属性用于限定请求参数条件，只有在请求中包含指定的参数或满足某些规则时，SpringMVC 才会将请求映射到对应的方法

```java
// 请求中必须包含 username 参数，否则不会匹配到这个方法，并且一般前端页面会报 400 错误
@RequestMapping(value = "/test", params = "username")
```

四种用法：

- @RequestMapping(value="/login", params={"username", "password"}) 表示：请求参数中必须包含 username 和 password，才能与当前标注的方法进行映射
- @RequestMapping(value="/login", params={"!username", "password"}) 表示：请求参数中不能包含username参数，但必须包含password参数，才能与当前标注的方法进行映射
- @RequestMapping(value="/login", params={"username=admin", "password"}) 表示：请求参数中必须包含username参数，并且参数的值必须是admin，另外也必须包含password参数，才能与当前标注的方法进行映射
- @RequestMapping(value="/login", params={"username!=admin", "password"}) 表示：请求参数中必须包含username参数，但参数的值不能是admin，另外也必须包含password参数，才能与当前标注的方法进行映射

****
### 2.7 headers 属性

headers 属性用于根据请求头（Header）信息限制请求的映射规则，只有当请求中包含符合指定规则的请求头时，方法才会被调用，例如：

```java
// 只有包含请求头 Content-Type: application/json 的请求才能访问
@RequestMapping(value = "/headerTest", headers = "Content-Type=application/json")
public String jsonOnly() {
}

// 请求头中必须包含 X-Token 请求头
@RequestMapping(value = "/headerTest", headers = "X-Token")
public String tokenPresent() {
}

// 请求头中不能包含 X-Debug 请求头
@RequestMapping(value = "/headerTest", headers = "!X-Debug")
public String noDebugHeader() {
}

// 请求头中 Accept 不能是 text/html
@RequestMapping(value = "/headerTest", headers = "Accept!=text/html")
public String notHtml() {
}
```

****
# 三. 获取请求提交数据

## 1. 使用原始的 servlet 获取

使用这种方法可以获取到表单提交的数据，但是较为麻烦，必须依赖原始 servlet 的 HttpServletRequest 和 HttpServletResponse

```java
@PostMapping(value="/registerServlet")
public String registerServlet(HttpServletRequest request){
    // 通过当前请求对象获取提交的数据
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String sex = request.getParameter("sex");
    String[] hobbies = request.getParameterValues("hobby");
    String intro = request.getParameter("intro");
    System.out.println(username + "," + password + "," + sex + "," + Arrays.toString(hobbies) + "," + intro);
    return "success";
}
```

****
## 2. 使用 RequestParam注解

将请求参数与方法上的形参映射，获取 GET 请求 URL 中的查询字符串或 POST 表单提交时的表单字段

```text
http://localhost:8080/user/login?username=Tom&password=123

<form action="/user/login" method="post">
  <input type="text" name="username">
  <input type="password" name="password">
</form>
```

基本用法：

```java
@PostMapping(value = "/registerParam")
public String register(
        @RequestParam(value="username")
        String username,
        @RequestParam(value="password")
        String password,
        @RequestParam(value="sex")
        String sex,
        @RequestParam(value="hobby")
        String[] hobby,
        @RequestParam(name="intro")
        String intro) {
    System.out.println(username);
    System.out.println(password);
    System.out.println(sex);
    System.out.println(Arrays.toString(hobby));
    System.out.println(intro);
    return "success";
}
```

常用参数：

- value 或 name：指定请求参数的名称，必须与表单或 URL 中参数名对应
- required：是否是必须传递的参数，默认值为 true，不传会报错
- defaultValue：指定默认值，传参为空或缺失时使用该值（设置后 required 自动为 false）

1、参数名一致时，可省略 value

```java
@GetMapping("/query")
public String query(@RequestParam String keyword) {
    return "...";
}

// 等同于
@GetMapping("/query")
public String query(@RequestParam("keyword") String keyword)
```

2、设置默认值

当浏览器访问 /search?page=2，参数为 2；如果访问 /search，参数自动使用默认值 1

```java
@GetMapping("/search")
public String search(@RequestParam(value = "page", defaultValue = "1") int page) {
    System.out.println("当前页：" + page);
    return "search";
}
```

3、非必传参数

如果访问 /user?id=100，输出 100；如果访问 /user，不会报错，id == null

```java
@GetMapping("/user")
public String getUser(@RequestParam(value = "id", required = false) Integer id) {
    System.out.println("用户ID：" + id);
    return "userInfo";
}
```

****
## 3. 使用 pojo 类接收请求参数

当前端发送的是表单数据,SpringMVC 会自动将表单字段与 POJO 中同名属性进行匹配，调用其 setter 方法进行赋值,所以前端表单字段要与 POJO 类的字段一致（实际上是 setter 方法名一致）

如果是嵌套对象的话，就需要修改一下前端表单的写法，例如：

```java
@PostMapping("/register")
public String register(User user) {
    System.out.println("用户名: " + user.getUsername());
    System.out.println("城市: " + user.getAddress().getCity());
    System.out.println("街道: " + user.getAddress().getStreet());
    return "success";
}
```

```html
<form action="/register" method="post">
    用户名：<input type="text" name="username"><br>
    城市：<input type="text" name="address.city"><br>
    街道：<input type="text" name="address.street"><br>
    <input type="submit" value="提交">
</form>
```

****
## 4. @RequestHeader 注解

@RequestHeader 注解用于将 HTTP 请求头中的值绑定到处理方法的参数上，它也有三个属性：value、required、defaultValue，和 RequestParam 一样

```java
@RequestHeader(value = "请求头名称", required = true/false, defaultValue = "默认值")
```

```java
@PostMapping("/register")
public String register(User user, 
                       @RequestHeader(value="Referer", required = false, defaultValue = "") 
                       String referer){
    System.out.println(user);
    System.out.println(referer);
    return "success";
}
```

****
## 5. @CookieValue 注解

@CookieValue 是 Spring 框架提供的一个注解，用于将客户端请求中携带的 Cookie 值注入到控制器方法的参数中，
在 Web 应用中，Cookie 通常用于实现用户登录状态保持、偏好设置存储、身份标识等功能

```java
@GetMapping("/registerCookie")
public String register(User user,
                       @RequestHeader(value="Referer", required = false, defaultValue = "")
                       String referer,
                       @CookieValue(value="id", required = false, defaultValue = "2222222222")
                       String id){
    System.out.println(user);
    System.out.println(referer);
    System.out.println(id);
    return "success";
}
```

```html
<script type="text/javascript">
    function sendCookie(){
        document.cookie = "id=123456789; expires=Thu, 18 Dec 2025 12:00:00 UTC; path=/";
        document.location = "/springmvc/user/registerCookie";
    }
</script>
<button onclick="sendCookie()">向服务器端发送Cookie</button>
```

****
## 6. 请求的中文乱码问题

### 6.1 GET 请求乱码

GET 请求的参数是通过 URL 传递的，浏览器会将 URL 中的中文编码成 UTF-8（或浏览器默认编码），但低版本的 Tomcat 默认用 ISO-8859-1 来解码 URL，就会导致读取时乱码，而高版本的 Tomcat，例如 9和10，
在默认情况下 URIEncoding 使用的就是 UTF-8 的编码方式，所以没有乱码问题

可以在 web.xml 文件中配置 Tomcat 的 URI 编码为 UTF-8

```xml
<Connector URIEncoding="UTF-8" />
```

### 6.2 POST 请求乱码

POST 表单提交时，参数通过请求体（body）传输，若服务端未设置正确的读取编码，会默认使用 ISO-8859-1 解析请求体，导致乱码，可以通过 `request.setCharacterEncoding("UTF-8");`解决，
但 Tomcat 10 中不用考虑，它在底层设置了请求体采用 UTF-8 的方式，响应时也采用 UTF-8。

- 在 DispatcherServlet 执行前通过过滤器解决乱码问题，而 SpringMVC 内置了这种过滤器，通过 web.xml 文件配置即可

```java
// isForceRequestEncoding 和 isForceResponseEncoding 初始值为 false，
// 通过 xml 文件将这两个值设为 true 就可以在 DispatcherServlet 执行前指定编码格式
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String encoding = this.getEncoding();
    if (encoding != null) {
        if (this.isForceRequestEncoding() || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }

        if (this.isForceResponseEncoding()) {
            response.setCharacterEncoding(encoding);
        }
    }

    filterChain.doFilter(request, response);
}
```

```xml
<!--字符编码过滤器-->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <!--设置字符集-->
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <!--让请求体的编码方式强制使用上面的字符集-->
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <!--让响应体的编码方式强制使用上面的字符集-->
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

****
# 四. Servlet 中的三个域对象

- 请求域：request
- 会话域：session
- 应用域：application

三个域都有以下三个方法，主要是通过 setAttribute + getAttribute 方法来完成在域中数据的传递和共享：

```java
// 向域中存储数据
void setAttribute(String name, Object obj);

// 从域中读取数据
Object getAttribute(String name);

// 删除域中的数据
void removeAttribute(String name);
```

## 1. request 域对象

request 域对象就是指当前 HTTP 请求的作用范围内的数据容器，本质是一个接口：`javax.servlet.http.HttpServletRequest`，request 对象代表了一次请求，一次请求一个 request。
使用请求域的业务场景：在A资源中通过转发的方式跳转到B资源，因为是转发，所以从A到B就是一次请求，如果想让A资源和B资源共享同一个数据，可以将数据存储到 request 域中

- SpringMVC 中 request 域共享数据有以下几种方式：

1. 使用原生Servlet API方式
2. 使用Model接口
3. 使用Map接口
4. 使用ModelMap类
5. 使用ModelAndView类

这些数据的生命周期仅存在于一次 HTTP 请求的过程中，也就是说仅在:控制器 -> 视图页面之间传递数据与请求转发（forward）之间共享数据，不适用于重定向（redirect，服务器或客户端在接收到请求后，不直接返回目标内容，而是告诉浏览器去访问另一个URL）

### 1.1 使用原生 Servlet API 方式

在 Controller 的方法上使用 HttpServletRequest：

```java
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request){
    // 向request域中存储数据
    request.setAttribute("testRequestScope", "在SpringMVC中使用原生Servlet API实现request域数据共享");
    return "viewServlet";
}
```

****
### 1.2 使用 Model 接口

Model 接口是用于向请求域中传递数据的核心接口，它是控制器方法与视图之间进行数据交互的桥梁，用于封装控制器处理结果中需要传递给视图的数据（本质上是对 HttpServletRequest.setAttribute() 方法的封装），
更容易做单元测试（不依赖 Tomcat）

```java
@RequestMapping("/testModel")
public String testModel(Model model){
    // 向request域中存储数据
    model.addAttribute("testRequestScope", "在SpringMVC中使用Model接口实现request域数据共享");
    // 等价于
    // request.setAttribute("testRequestScope", "在SpringMVC中使用Model接口实现request域数据共享");
    return "view";
}
```

****
### 1.3 使用 Map 接口

SpringMVC 的核心类 ModelAndViewContainer 会收集控制器中的模型数据，无论用的是 Model、ModelMap 还是 Map，它都会将数据统一合并，最后写入 request 域中，
但 Map 的方式不具备一些常用的 API 且可读性较差，不推荐使用

```java
// 控制器执行后，DispatcherServlet 最终将这些数据存入 request 域
request.setAttribute("msg", "通过 Map 向 request 域传值");
```

```java
@RequestMapping("/testMap")
public String testMap(Map<String, Object> map){
    // 向request域中存储数据
    map.put("testRequestScope", "在SpringMVC中使用Map接口实现request域数据共享");
    return "view";
}
```

****
### 1.4 使用 ModelMap 类

ModelMap 是 SpringMVC 提供的一个用于封装模型数据的类，实质上是一个继承自 LinkedHashMap 的类，它具有 Map 的特性可以连续添加多个属性，也拥有 Model 的可读性，
底层数据最终也会写入 request 域

```java
modelMap.addAttribute("a", 1)
        .addAttribute("b", 2)
        .addAttribute("c", 3);
```

```java
@RequestMapping("/testModelMap")
public String testModelMap(ModelMap modelMap){
    // 向request域中存储数据
    modelMap.addAttribute("testRequestScope", "在SpringMVC中使用ModelMap实现request域数据共享");
    return "view";
}
```

这三种方式最后实例化的对象都是同一个：org.springframework.validation.support.BindingAwareModelMap

****
### 1.5 使用 ModelAndView 类

在 SpringMVC 框架中为了更好的体现MVC架构模式，底层提供了一个 ModelAndView 类，它封装了 Model 和 View，也就是说这个类既封装业务处理之后的数据，也体现了跳转到哪个视图。
SpringMVC 执行流程中会自动从 ModelAndView 拆出视图名和数据，然后把模型数据存入 request 域并根据视图名解析为实际页面

- ModelAndView 的三种常见写法：

1. 构造函数传参

```java
@RequestMapping("/mv1")
public ModelAndView mv1() {
    // 视图名：user；传入“key = username，value = 张三”
    return new ModelAndView("user", "username", "张三"); 
}
```

2. 连续调用写法

```java
@RequestMapping("/mv2")
public ModelAndView mv2() {
    ModelAndView mav = new ModelAndView("user");
    mav.addObject("username", "李四");
    mav.addObject("age", 22);
    return mav;
}
```

3. 使用 ModelMap 或 Map 配合视图名返回

```java
@RequestMapping("/mv3")
public ModelAndView mv3() {
    Map<String, Object> map = new HashMap<>();
    map.put("username", "王五");
    map.put("gender", "男");
    return new ModelAndView("user", map);
}
```

- 使用 ModelAndView 需要注意：

1. 方法的返回值类型不是String，而是ModelAndView对象
2. ModelAndView不是出现在方法的参数位置，而是在方法体中new的
3. 需要调用addObject向域中存储数据
4. 传参时没指定视图名就需要调用setViewName设置视图的名字

```java
@RequestMapping("/testModelAndView")
public ModelAndView testModelAndView(){
    // 创建“模型与视图对象”
    ModelAndView modelAndView = new ModelAndView();
    // 绑定数据
    modelAndView.addObject("testRequestScope", "在SpringMVC中使用ModelAndView实现request域数据共享");
    // 绑定视图
    modelAndView.setViewName("view");
    // 返回
    return modelAndView;
}
```

> 在 Spring MVC 中，无论控制器方法返回的是逻辑视图名（String）、模型数据（Model/Map/ModelMap），还是显式的 ModelAndView，
> 最终框架都会将它们封装为 ModelAndView 对象，由 DispatcherServlet 的核心方法 doDispatch() 进行调度处理，
> 经过视图解析器解析为 View 对象后，调用它的 render() 方法与 Model 对象结合

```java
// 调用 Controller 中对应的方法，然后封装成 ModelAndView
ModelAndView mv = handlerAdapter.handle(request, response, handler);
// 将逻辑视图转换成对应的 View 对象
View view = viewResolver.resolveViewName(mv.getViewName(), locale);
// 将数据和视图组合，生成响应
view.render(mv.getModel(), request, response);
```

```text
用户发起请求
      ↓
DispatcherServlet（前端控制器）收到请求
      ↓
HandlerMapping → 查找对应的处理器（Controller 方法）
      ↓
HandlerAdapter → 执行 Controller 方法
      ↓
Controller 方法返回（逻辑视图名 或 ModelAndView）
      ↓
收集视图名 + 模型数据（BindingAwareModelMap）
      ↓
DispatcherServlet 接收结果 → 封装为 ModelAndView 对象
      ↓
ViewResolver → 将逻辑视图名解析为 View 对象
      ↓
View.render(model, request, response)
      ↓
生成 HTML 页面响应用户
```

****
## 2. session 域对象

在 Java Web（如 SpringMVC）中，HttpSession 是由服务器端创建和管理的一个会话对象，用来表示用户与服务器之间的一次会话。
从用户打开浏览器访问网站开始，到关闭浏览器，这段时间就是一次会话（Session）：

- 每一个会话对应一个 HttpSession 对象
- 每个 HttpSession 对象都有一个唯一标识：JSESSIONID
- JSESSIONID 默认通过浏览器中的 Cookie 存储在客户端
- 浏览器关闭或 Session 超时后，JSESSIONID 失效，会话结束
- Session 本质上是在服务器端开辟的一块“会话级内存空间”

使用会话域的业务场景：

1. 跨请求共享数据（如重定向跳转）：请求从A资源重定向（Redirect）到B资源，因为重定向会产生两个请求对象，所以无法用 request.setAttribute() 传递数据，此时可以使用 session.setAttribute() 存储数据，在B资源中通过 session.getAttribute() 读取
2. 登录状态管理：登录成功后，将用户信息存储到 Session 中，以此判断用户是否已登录
3. 购物车等场景：用户在多个页面添加商品，但结账时是统一提交的，这些商品信息就可以存在 session 中做统一管理

- Session 生命周期：

1. 会话创建：第一次通过 request.getSession() 会自动创建 HttpSession 对象
2. 设置最大空闲时间：`session.setMaxInactiveInterval(1800);`，以秒为单位，当超过后则自动销毁
3. 手动销毁：通过 session.invalidate() 主动销毁会话
4. 浏览器关闭：客户端 Cookie 中 JSESSIONID 失效，服务器中 Session 难以再被访问到

- 通过原生 servlet 获取：

```java
@RequestMapping("/testSessionScope1")
public String testServletAPI(HttpSession session) {
    // 向会话域中存储数据
    session.setAttribute("testSessionScope1", "使用原生Servlet API实现session域共享数据");
    return "view";
}
```

- 使用 @SessionAttributes 注解：

@SessionAttributes注解使用在Controller类上，标注了当key是 x 或者 y 时，数据将被存储到会话session中。如果没有@SessionAttributes注解，默认存储到request域中

```java
@Controller
@SessionAttributes(value = {"x", "y"})
public class SessionScopeTestController {

    @RequestMapping("/testSessionScope2")
    public String testSessionAttributes(ModelMap modelMap){
        // 向session域中存储数据
        modelMap.addAttribute("x", "我是埃克斯");
        modelMap.addAttribute("y", "我是歪");

        return "view";
    }
}
```

****
## 3. application 域对象

SpringMVC 中的 application 域对象，就是 ServletContext 对象，表示整个 Web 应用的上下文环境（所有用户共享），作用范围是整个服务器生命周期，通过以下方式获取:

```java
@RequestMapping("/testApplicationScope")
public String testApplicationScope(HttpServletRequest request){
    // 获取ServletContext对象
    ServletContext application = request.getServletContext();
    // 向应用域中存储数据
    application.setAttribute("applicationScope", "应用域中的一条数据");
    return "view";
}
```

或者直接定义为成员变量：

```java
@Controller
public class AppController {
    @Autowired
    private ServletContext application;

    @GetMapping("/setGlobal")
    public String setGlobalData() {
        application.setAttribute("siteName", "SpringMVC学习网");
        return "success";
    }

    @GetMapping("/getGlobal")
    public String getGlobalData(Model model) {
        String name = (String) application.getAttribute("siteName");
        model.addAttribute("site", name);
        return "display";
    }
}
```

****
## 4. PageContext 域对象（SpringMVC 中没有独立的 PageContext 域对象）

> PageContext 是 Java EE 中 JSP（Java Server Pages）技术特有的作用域对象，用于在 JSP 页面中访问其他域对象（如 request、session、application）和管理 JSP 中的一些信息，代表当前 JSP 页面的上下文对象

因为 SpringMVC 是基于 Servlet 的控制器模型，它不依赖 JSP 页面语法，所以它内部不存在这个域对象

****
# 五. 视图

- Spring MVC 视图支持可配置：

在 Spring MVC 中，视图 View 是支持定制的，例如在第一个程序中的 [springmvc.xml](./Demo1-First/src/main/webapp/WEB-INF/springmvc-servlet.xml) 文件中进行了相关配置，这种设计是完全符合OCP开闭原则的，视图V iew 和框架是解耦合的，耦合度低扩展能力强，视图 View 可以通过配置文件进行灵活切换

## 1. Spring MVC 支持的常见视图

1. InternalResourceView：内部资源视图（Spring MVC框架内置的，专门为 JSP 模板语法准备的），常用于传统的服务端渲染网页

```xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".jsp" />
</bean>
```

2. RedirectView：重定向视图（Spring MVC框架内置的，用来完成重定向效果），不通过模板引擎直接跳转 URL 或页面路径

```java
// 重定向（客户端跳转）
return "redirect:/login";

// 转发（服务器内部跳转）
return "forward:/WEB-INF/views/welcome.jsp";
```

3. ThymeleafView：Thymeleaf视图（第三方的，为 Thymeleaf 模板语法准备的）
4. FreeMarkerView：FreeMarker视图（第三方的，为 FreeMarker 模板语法准备的）
5. VelocityView：Velocity视图（第三方的，为 Velocity 模板语法准备的，早期广泛使用，但不再推荐）
6. PDFView：PDF视图（第三方的，专门用来生成 pdf 文件视图）
7. ExcelView：Excel视图（第三方的，专门用来生成 excel 文件视图）

****
## 2. 实现视图机制的核心类与接口

1、DispatcherServlet类（前端控制器）：

> 在整个Spring MVC执行流程中，负责中央调度，核心方法：doDispatch

2、ViewResolver接口（视图解析器）：

> 负责将逻辑视图名转换为物理视图名，最终创建 View 接口的实现类，即视图实现类对象，核心方法：resolveViewName

3、View接口（视图）:

> 负责将模型数据 Model 渲染为视图格式（HTML 代码）并输出到客户端（它负责将模板语言转换成 HTML 代码），核心方法：render

4、ViewResolverRegistry（视图解析器注册器）：

> 负责在web容器（Tomcat）启动的时候完成视图解析器的注册，如果有多个视图解析器，则会将视图解析器对象按照 order 的配置（越小优先级越高）放入 List 集合

- 整体流程：

```text
[客户端请求] → DispatcherServlet.doDispatch()：整个流程的发起者
                       ↓
             HandlerAdapter.handle()
                       ↓
           返回 ModelAndView（视图名 + 模型数据）
                       ↓
         processDispatchResult() → render()
                       ↓
     ViewResolver.resolveViewName() → View（视图对象）
                       ↓
           View.render() → 输出 HTML / JSON 响应
```

```java
public class DispatcherServlet extends FrameworkServlet {
    // 前端控制器的核心方法，处理请求，返回视图，渲染视图，都是在这个方法中完成的
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 根据请求路径调用映射的处理器方法，处理器方法执行结束之后，返回逻辑视图名称
        // 返回逻辑视图名称之后，DispatcherServlet 会将逻辑视图名称 viewName + Model，将其封装为 ModelAndView 对象
        mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

        // 对返回的 ModelAndView 进行处理
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    
    // 这里是判断有没有异常、有没有 ModelAndView，决定是否渲染页面
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
                                       @Nullable Exception exception) throws Exception {
        //渲染页面（将模板字符串转换成 html 代码响应到浏览器）
        render(mv, request, response); // 调用的是本类中的 render() 方法
    }

    // 真正进入视图解析和渲染阶段
    protected void render(ModelAndView my, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = mv.getViewName();
        // 这个方法的作用是将逻辑视图名称转换成物理视图名称，并且最终返回视图对象 View
        View view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        // 真正的将模板字符串转换成 html 代码，并将 html 响应给浏览器
        view.render(mv.getModelInternal(), request, response);
    }

    protected View resolveViewName(String viewName, @Nullable Map<String, Object> model,
                                   Locale locale, HttpservletRequest request) throws Exception {
        //其实这一行代码才是真正起作用的：将逻辑视图名称转换成物理视图名称，并且最终返回视图对象 view
        ViewResolver viewResolver; // 如果使用的是 ThymeleafViewResolver， 那么底层会创建一个 ThymeleafViewResolver 对象
        // viewResolver 实际是一个列表（List<ViewResolver>），可能存在多个视图对象，Spring 会依次调用直到找到非 null 的 View
        View view = viewResolver.resolveViewName(viewName, locale); // 返回的视图对象就是 ThymeleafView 对象
        return view;
    }
}
```

如果想要实现自己的视图，就需要实现以下两个接口，然后重写对应的这两个方法

```java
// 负责视图解析
public interface ViewResolver { // 如果使用Thymeleaf，那么该接口的实现类就是：ThymeleafViewResolver
    // 这个方法就是将逻辑视图名称转换成物理视图名称，并且最终返回视图对象 view
    View resolveViewName(String viewName, Locale locale) throws Exception;
}
```

```java
// 负责将模板字符串转换成 html 代码
public interface View {
    void render(@Nullable Map<String, ?> model,HttpServletRequest request,HttpServletResponse response) throws Exception;
}
```

****
## 3. 转发与重定向

### 3.1 请求转发（Forward）

请求转发是服务器内部的资源跳转行为，当前请求由服务器内部跳转到另一个资源处理，客户端并不知情，通常拥有以下特点：

- 同一个 request 对象
- 不改变浏览器地址栏
- 数据可以保存在 request 域中共享
- 页面跳转速度较快
- 通常用于跳转到内部资源（如 JSP 页面、另一个 Controller）

使用方式：

1、原生 Servlet：

```java
// 跳转到 .../b 
request.getRequestDispatcher("/b").forward(request,response);
```

2、SpringMVC 中：

DispatcherServlet 类的 resolveViewName 方法中的 viewResolver 就是 InternalResourceViewResolver 的实例，viewResolver 会调用父类的 buildView 方法，
进行路径的拼接，然后在 render 方法中执行 `RequestDispatcher rd = getRequestDispatcher(request); rd.forward(request, response);`

```java
// 返回逻辑视图名（默认是请求转发）
@RequestMapping("/forward1")
public String forwardTest1(Model model) {
    model.addAttribute("msg", "请求转发示例");
    return "success";  // 交给视图解析器，默认走 InternalResourceViewResolver，转发到 /WEB-INF/views/success.jsp
}

// 显式声明转发路径
@RequestMapping("/forward2")
public String forwardTest2() {
    return "forward:/hello";
}
```

****
### 3.2 重定向（Redirect）

重定向是由服务器指示浏览器重新发送一次请求到另一个资源，地址栏会改变并重新发出新的 HTTP 请求，通常拥有以下特点：

- 两次请求，request 不共享
- 地址栏会更新
- 不能共享 request 域中的数据
- 可以携带参数到下一个请求，但必须放在 URL 上
- 通常用于：登录成功后跳转主页、避免表单重复提交

使用方式：

```java
@RequestMapping("/redirect1")
public String redirectTest1() {
    return "redirect:/index"; // 触发浏览器重定向
}

@RequestMapping("/redirect2")
public String redirectTest2(RedirectAttributes attrs) {
    attrs.addAttribute("id", 123);
    attrs.addFlashAttribute("msg", "重定向成功"); // Flash 属性，不会放进 URL，但会自动存入 session 中，临时保存
    return "redirect:/result";
}
```

如果要跨域就需要写上全路径：

```java
@RequestMapping("/a")
public String a(){
    return "redirect:http://localhost:8080/springmvc2/b";
}
```

#### Flash Attributes（闪存属性，为重定向共享数据设计）

重定向会导致 request 对象失效，`request.setAttribute()` 也就无法使用，Spring 通过 RedirectAttributes 接口提供了一种机制，可以在重定向中传递临时数据，这种数据就叫做 Flash 属性（Flash Attributes），
Flash Attributes 是一种保存在 Session 中的临时数据，它在一次请求结束后自动生效，并在下一个请求中可用，读取一次之后立即销毁，非常适合用于一次性提示信息等

```java
// 添加闪存属性（在第一次请求中设置）
@PostMapping("/save")
public String save(RedirectAttributes redirectAttributes) {
    // addFlashAttribute 放入闪存（临时）数据
    redirectAttributes.addFlashAttribute("msg", "保存成功！");
    return "redirect:/result"; // 重定向到下面的路径
}

// 获取闪存属性（在第二次请求中使用）
@GetMapping("/result")
public String resultPage(Model model) {
    // 自动从 session 中读取并绑定到 model，读取后立即移除
    return "result"; // 跳转到对应页面
}
```

****
## 4. 视图控制器（`<mvc:view-controller>`）

视图控制器是 Spring MVC 中一种简单的控制器映射方式，它不需要编写任何业务逻辑，只负责将请求直接映射到某个视图（页面），适合做简单的页面跳转，可以省去写空的 Controller 类和方法，快速将请求 URL 映射到指定的视图名

```xml
<!-- 只做请求映射，跳转到视图 -->
<mvc:view-controller path="/login" view-name="loginPage" />
```

- path：请求路径（浏览器请求 URL）
- view-name：视图名称（对应视图解析器解析的视图，比如 JSP 文件名）

当用户访问 .../login，直接返回视图名 loginPage，比如会被解析为 /WEB-INF/views/loginPage.html，不过需要注意的是，配置了这个后，所有类的 @Controller 注解都会失效，需要添加额外的标签让不让它们失效

```xml
<mvc:annotation-driven/>
```

****
## 5. 访问静态资源

静态资源指的是网站中不需要后端动态处理，直接由服务器返回给客户端的文件，如：

- 图片（jpg、png、gif） 
- CSS 样式表 
- JavaScript 文件
- HTML 静态页面 
- 字体文件等

Spring MVC默认会将所有请求（包括静态资源请求）都通过DispatcherServlet来处理，如果没有额外配置，访问静态资源时可能出现404错误（找不到资源）或静态资源被DispatcherServlet拦截，
因为 DispatcherServlet 会寻找匹配的 Controller 来处理这个请求，但对应静态资源通常是不会配置 Controller 的，所以就可能导致资源无法正确返回

### 5.1 使用默认 Servlet 处理静态资源

这是最简单的方式，它会把静态资源请求交给服务器默认的Servlet来处理，当DispatcherServlet处理不了请求时（比如静态资源），
它会将请求转发给Servlet容器中的默认Servlet，让容器来直接响应静态资源

首先需要在springmvc.xml文件中添加以下配置，开启默认Servlet处理静态资源功能：

```xml
<!-- 开启注解驱动 -->
<mvc:annotation-driven />

<!--开启默认Servlet处理-->
<mvc:default-servlet-handler>
```

****
### 5.2 使用 `<mvc:resources>` 标签配置静态资源

这个方式更灵活，可以指定URL路径映射到资源文件夹，在 springmvc.xml 文件中配置即可：

```xml
<!-- 开启注解驱动 -->
<mvc:annotation-driven />
<!--这个也必须开启注解驱动才能使用-->
<mvc:resources mapping="/static/**" location="/static/" />
```

- mapping：访问路径，比如/static/**，表示匹配所有/static/路径下的请求
- location：资源文件存放的位置，可以是类路径路径、webapp 路径或者文件系统路径

****
# 六. RESTful 变成风格

## 1. 什么是 RESTful

REST 全称是 Representational State Transfer（表述性状态转移），是一种架构风格，通过统一的 URI 定位资源，通过 HTTP 动作（GET、POST、PUT、DELETE）对资源进行操作。
RESTful 是遵循 REST 风格的 Web 服务设计方式，即以资源为中心，使用统一的 URI 和标准的 HTTP 方法来操作资源

RESTful 对一个 Web 服务接口规定以下内容：

1. 一切皆资源，例如服务中的每个实体（如用户、商品、订单等）都被当作资源，每个资源都由一个 URI 唯一标识

```html
/users        表示用户集合
/users/123    表示 ID 为 123 的用户
/orders/456   表示订单 ID 为 456 的订单
```

2. RESTful 规定使用标准的 HTTP 方法（动词）来表示对资源的操作

- GET：查询资源，例如：GET /users/123
- POST：创建资源，例如：POST /users
- PUT：更新资源（整体），例如：PUT /users/123
- PATCH：更新资源（部分），例如：PATCH /users/123
- DELETE：删除资源，例如：DELETE /users/123

3. URI 应该只表示资源本身，而不是操作或动作，并且使用复数名词表示资源集合，使用嵌套结构表示资源间关系

```html
/users/123/orders      // 表示用户 123 的订单
/products/456/reviews  // 表示商品 456 的评价

/getUser?id=123 // 不使用这种
```

4. RESTful 的理想状态要求通过响应中提供的链接指导客户端进行下一步操作，比如在返回用户详情时，同时返回下一个要执行的链接

****
## 2. RESTful 的简单使用

- 根据 id 查询

```java
@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
public String getById(@PathVariable("id") Integer id){
    System.out.println("根据用户id查询用户信息，用户id是" + id);
    return "ok";
}
```

- 查询所有

```java
@RequestMapping(value = "/api/user", method = RequestMethod.GET)
public String getAll(){
    System.out.println("查询所有用户信息");
    return "ok";
}
```

- 新增

```java
@RequestMapping(value = "/api/user", method = RequestMethod.POST)
public String save(){
    System.out.println("保存用户信息");
    return "ok";
}
```

- 修改

使用修改必须发送的是 PUT 请求，通过配置文件，将 POST 请求转换成 PUT 请求，

```xml
<!--隐藏的HTTP请求方式过滤器，用来将POST转换成PUT/DELETE请求-->
<filter>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

```xml
<form th:action="@{/api/user}" method="post">
    <!--隐藏域的方式提交 _method=put,必须这样写-->
    <input type="hidden" name="_method" value="put">
    用户名：<input type="text" name="username"><br>
    <input type="submit" th:value="修改">
</form>
```

```java
@RequestMapping(value = "/api/user", method = RequestMethod.PUT)
public String update(String username){
    System.out.println("修改用户信息，用户名：" + username);
    return "ok";
}
```

****
## 3. HiddenHttpMethodFilter 过滤器

标准 HTML 的 <form> 表单只支持两种请求方式，GET 和 POST，但在 RESTful 风格中，通常还需要支持 PUT 和 DELETE，SpringMVC 提供的过滤器 HiddenHttpMethodFilter 就是专门为了解决这个问题，
它可以拦截请求，如果发现是一个 POST 请求，且带有 `_method` 参数，就把这个请求伪装成 PUT 或 DELETE 请求

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    HttpServletRequest requestToUse = request;
    // 对表单中提交的数据进行判断，只对 POST 处理，
    if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
        // 提取 _method 参数的值（methodParam 的默认值就是 _method），例如 _method = put，那么提取的就是 put
        String paramValue = request.getParameter(this.methodParam);
        if (StringUtils.hasLength(paramValue)) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            if (ALLOWED_METHODS.contains(method)) { // 只有请求方式是 PUT,DELETE,PATCH的时候会创建HttpMethodRequestWrapper对象
                // 把获取到的新的请求方式传递给构造器，将原始请求伪装为一个新的请求方法
                requestToUse = new HttpMethodRequestWrapper(request, method);
            }
        }
    }
    filterChain.doFilter(requestToUse, response);
}

public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
    super(request);
    this.method = method; // 将 POST 转换成 PUT
}
```

不过字符编码过滤器执行之前不能调用 request.getParameter() 方法，如果提前调用了，乱码问题就无法解决了，
所以request.setCharacterEncoding()方法的执行必须在所有request.getParameter()方法之前执行，而上面的过滤器又执行了这个方法，所以在web文件中配置时需要注意前后顺序，
应该先配置CharacterEncodingFilter，然后再配置HiddenHttpMethodFilter

****
## 4. 使用 RESTFul 风格实现简单的用户管理系统




