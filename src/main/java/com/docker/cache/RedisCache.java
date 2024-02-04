package com.docker.cache;

import com.docker.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.applet.AppletContext;

/**
 * @description: 自定义 Redis cache
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@Slf4j
public class RedisCache implements Cache {

    /*@Resource
    RedisTemplate<Object,Object> redisTemplate;*/


    //此处id打印可以看到，即为当前放入缓存的 mapper 的namespace
    private final String id;

    public RedisCache(String id){
        System.out.println("id = " + id);
        this.id = id;
    }

    //返回cache的唯一标识
    @Override
    public String getId() {
        return this.id;
    }

    //缓存中放入数据
    @Override
    public void putObject(Object key, Object value) {
        /*

         key = -368409926:4248786120:com.docker.dao.UserDao.selectAll:0:2147483647:select *  from user_info;:MybatisSqlSessionFactoryBean
         value = [User(id=1, name=zs, age=15, className=1, subject=语文), User(id=2, name=ls, age=15, className=1, subject=数学), User(id=3, name=zs, age=15, className=1, subject=数学)]

         */
        System.out.println("key = " + key.toString());
        System.out.println("value = " + value);

        //首字母必须默认为小写
        RedisTemplate redisTemplate = (RedisTemplate)ApplicationContextUtils.getBean("redisTemplate");
        //redis 的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //使用redis中的hash类型作为存储模型 key 当前mapper的namespace hashkey为方法key value为值
        redisTemplate.opsForHash().put(id,key.toString(),value);



    }

    //缓存中取出数据
    @Override
    public Object getObject(Object key) {
        /*

         key = -368409926:4248786120:com.docker.dao.UserDao.selectAll:0:2147483647:select *  from user_info;:MybatisSqlSessionFactoryBean

         */
        System.out.println("key = " + key.toString());
        RedisTemplate redisTemplate = (RedisTemplate)ApplicationContextUtils.getBean("redisTemplate");
        //redis 的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //根据key 从redis的hash类型中获取数据
        return  redisTemplate.opsForHash().get(id, key.toString());
        //return null;
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }
}
