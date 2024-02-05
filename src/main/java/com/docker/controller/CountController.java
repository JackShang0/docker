package com.docker.controller;

import com.docker.bean.User;
import com.docker.dao.StudentDao;
import com.docker.dao.UserDao;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    UserDao userDao;

    @Resource
    StudentDao studentDao;

    /**
     * docker集成redis 并操作成功
     * url:<a href="http://47.106.68.235:6665/hello">...</a>
     * @return 操作次数
     */
    @GetMapping("/hello")
    public String count(){
        Long increment = redisTemplate.opsForValue().increment("count-hello",1);
        return "有 "+increment+" 人访问了 hello 页面";
    }


    /**
     * mybatis的二级缓存案例
     * @return u
     */
    @PostMapping("/selectUser")
    public List<User> selectUser(){
        Cache cache;

        /*
        执行日志：第一次查询数据库，第二次查询命中缓存

         * SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3b5ae09e] was not registered for synchronization because synchronization is not active
         * Cache Hit Ratio [com.docker.dao.UserDao]: 0.0
         * JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@52560acc] will not be managed by Spring
         * ==>  Preparing: select * from user_info;
         * ==> Parameters:
         * <==    Columns: id, name, age, className, subject
         * <==        Row: 1, zs, 15, 1, 语文
         * <==        Row: 2, ls, 15, 1, 数学
         * <==        Row: 3, zs, 15, 1, 数学
         * <==      Total: 3
         * Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3b5ae09e]
         * Creating a new SqlSession
         * SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@21fbc935] was not registered for synchronization because synchronization is not active
         * As you are using functionality that deserializes object streams, it is recommended to define the JEP-290 serial filter. Please refer to https://docs.oracle.com/pls/topic/lookup?ctx=javase15&id=GUID-8296D8E8-2B93-4B9A-856E-0A65AF9B8C66
         * Cache Hit Ratio [com.docker.dao.UserDao]: 0.5
         * Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@21fbc935]
         */
        return userDao.selectAll();
    }


    @PostMapping("/selectUserById")
    public User selectUserById(){
        User user = userDao.selectById("1");
        System.out.println("user = " + user);
        System.out.println(" ==================== " );
        return userDao.selectById("1");
    }

    @PostMapping("/deleteById")
    public void deleteById(){
        userDao.deleteById("1");
    }

    @PostMapping("/saveUser")
    public void saveUser(){
        userDao.saveUser(new User().setName("ww").setAge("15").setClassName("1").setSubject("数学"));
    }


    @PostMapping("/selectStudent")
    public void selectStudent(){
        studentDao.selectAll();
    }
}
