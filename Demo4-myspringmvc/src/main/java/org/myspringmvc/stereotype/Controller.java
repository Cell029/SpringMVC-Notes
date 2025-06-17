package org.myspringmvc.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 该注解只能标注类
@Retention(RetentionPolicy.RUNTIME) // 该注解可被反射获取
public @interface Controller {
}
