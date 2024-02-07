package com.docker.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/6
 * @version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCache {
    String cacheNames() default "";

    String key() default "";

    //缓存时间（单位：秒，默认是无限期）
    //int time() default -1;
    long time() default 30L;
}
