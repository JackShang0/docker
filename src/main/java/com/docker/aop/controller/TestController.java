package com.docker.aop.controller;

import com.docker.aop.CacheFind;
import com.docker.aop.MyCache;
import com.docker.aop.MyCacheEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    //测试删除缓存
    @GetMapping("/deleteCache")
    @MyCacheEvict(cacheNames = "cacheTest", key = "userData")
    public String deleteCache(){
        System.out.println("deleteCache success");
        return "deleteCache success";
    }

    //测试新增缓存
    @GetMapping("/addCache")
    //@CacheFind
    @MyCache(cacheNames = "cacheTest", key = "userData")
    public String addCache(){
        System.out.println("addCache success");
        return "addCache success";
    }
}