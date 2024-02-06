package com.docker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@Slf4j
public class MyCacheEvictAop {

    @Resource
    private RedisService redisService;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(myCache)")
    public void pointCut(MyCacheEvict myCache){
    }

    /**
     * 环绕通知
     */
    @Around("pointCut(myCache)")
    public Object around(ProceedingJoinPoint joinPoint, MyCacheEvict myCache) {
        String cacheNames = myCache.cacheNames();
        String key = myCache.key();
        /**
         * 思路：
         * 1、拼装redis中存缓存的key值
         * 2、删除缓存
         * 3、执行目标接口业务代码
         * 4、再删除缓存
         */
        String redisKey = new StringBuilder(cacheNames).append(":").append(key).toString();
        String methodPath = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        Object result ;
        //删除缓存
        redisService.deleteObject(redisKey);
        try {
            //执行接口
            result = joinPoint.proceed();
            //删除缓存
            redisService.deleteObject(redisKey);
            log.info("访问接口：[{}]，缓存删除成功", methodPath);
        } catch (Throwable e) {
            log.error("发生异常:{}", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}