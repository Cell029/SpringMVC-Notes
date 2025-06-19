package com.cell.springmvc.config;

import com.cell.springmvc.interceptors.MyInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;
import java.util.Properties;

@Configuration
// 开启组件扫描
@ComponentScan("com.cell.springmvc.controller")
// 开启注解驱动
@EnableWebMvc
public class SpringMVCConfig implements WebMvcConfigurer {

    // 配置视图解析器
    @Bean
    public ThymeleafViewResolver getViewResolver(SpringTemplateEngine springTemplateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(springTemplateEngine);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver iTemplateResolver) { // 支持自动装配
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(iTemplateResolver);
        return templateEngine;
    }

    @Bean
    public ITemplateResolver templateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/thymeleaf/"); // 前缀
        resolver.setSuffix(".html"); // 后缀
        resolver.setTemplateMode(TemplateMode.HTML); // 模板模式
        resolver.setCharacterEncoding("UTF-8"); // 编码方式
        resolver.setCacheable(false); // 开发时关闭缓存，改动即可生效
        return resolver;
    }

    // 开启静态资源处理
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // 配置视图控制器 view-controller
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    // 异常处理器
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 可以配置多个异常处理器，这是其中一个
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        // 设置属性
        Properties prop = new Properties();
        prop.setProperty("java.lang.Exception", "tip");
        resolver.setExceptionMappings(prop);
        resolver.setExceptionAttribute("e");
        resolvers.add(resolver);
    }

    // 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        MyInterceptor myInterceptor = new MyInterceptor();
        registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("/index");
    }
}
