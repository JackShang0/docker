package com.docker.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //该注解对方法有效
@Retention(RetentionPolicy.RUNTIME) //表示运行期有效
public @interface CacheFind {
    /**
     * 对于key的设定：
     * 1.如果用户自己定义了key，那我们就用用户自己的key
     * 2.如果没有没有定义key，则自动生成key：包名.类名.方法名::第一个参数
     */
    //保存到redis中 key value 用户可以自己指定,也可以动态生成
    public String key() default "";

    public int seconds() default 0; //设定超时时间秒，0表示永不超时
}
