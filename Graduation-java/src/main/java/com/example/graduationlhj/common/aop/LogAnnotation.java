package com.example.graduationlhj.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {
    /**
     * 模块
     *
     * @return
     */
    String module() default "";

    /**
     * 操作
     *
     * @return
     */
    String operator() default "";
}
