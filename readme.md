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


