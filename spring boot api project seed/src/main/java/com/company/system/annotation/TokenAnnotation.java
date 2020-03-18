package com.company.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/17 16:51
 * @Decription 自定义token拦截器注解，表明给注解所注释的接口，都会被拦截器所拦截，并进行token验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenAnnotation {
}
