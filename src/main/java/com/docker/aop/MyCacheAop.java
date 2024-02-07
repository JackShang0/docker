package com.docker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/6
 * @version: 1.0
 */
@Aspect
@Component
@Slf4j
public class MyCacheAop {
    @Resource
    private RedisService redisService;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(myCache)")
    public void pointCut(MyCache myCache){
    }

    /**
     * 环绕通知
     */
    @Around("pointCut(myCache)")
    public Object around(ProceedingJoinPoint joinPoint, MyCache myCache) {
        String cacheNames = myCache.cacheNames();
        String key = myCache.key();
        System.out.println("key = " + key);
        Long time = myCache.time();
        System.out.println("time = " + time);
        /**
         * 思路：
         * 1、拼装redis中存缓存的key值
         * 2、看redis中是否存在该key
         * 3、如果存在，直接取出来返回即可，不需要执行目标方法了
         * 4、如果不存在，就执行目标方法，然后将缓存放一份到redis中
         */
        String redisKey = new StringBuilder(cacheNames).append(":").append(key).toString();
        String methodPath = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        Object result ;
        if (redisService.exists(redisKey)){
            log.info("访问接口：[{}]，直接从缓存获取数据", methodPath);
            return redisService.getCacheObject(redisKey);
        }
        try {
            //执行接口
            result = joinPoint.proceed();
            //接口返回结果存Redis
            redisService.setCacheObject(redisKey, result, time, TimeUnit.SECONDS);
            log.info("访问接口：[{}]，返回值存入缓存成功", methodPath);
        } catch (Throwable e) {
            log.error("发生异常:{}", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}