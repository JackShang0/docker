package com.docker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/1/30
 * @version: 1.0
 */
@RestController
public class CountController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/hello")
    public String count(){

        Long increment = redisTemplate.opsForValue().increment("count-hello",1);
        return "有 "+increment+" 人访问了 hello 页面";
    }
}
