package com.docker.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 获取 SpringBoot 创建好的对象工厂
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@Configuration
public class ApplicationContextUtils implements ApplicationContextAware {

    /*
        1.implements ApplicationContextAware
        2.定义 ApplicationContext applicationContext 将当前获取到的 context 保留下来

     */


    //保留下来的对象工厂
    private static ApplicationContext applicationContext;

    /**
     * 将创建好的对象工厂以参数的形式传给当前这个类
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException factory
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 提供在工厂中获取对象的方法  根据对象的名字获取工厂中的对象
     * @param beanName 对象名字
     * @return 对象
     */
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
