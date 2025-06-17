package org.myspringmvc.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD}) // 该注解可以标注在类和方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /*
    * @RequestMapping(value = "/login", method = RequestMethod.GET)
    */

    // 指定请求路径
    String[] value();
    // 指定请求方式，枚举类型
    RequestMethod method();
}
