package com.docker.cache;

import com.docker.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;


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
        System.out.println("key = " + DigestUtils.md5DigestAsHex(key.toString().getBytes(StandardCharsets.UTF_8)));
        System.out.println("value = " + value);

        RedisTemplate<Object,Object> redisTemplate = getRedisTemplate();
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //使用redis中的hash类型作为存储模型 key 当前mapper的namespace hashkey为方法key value为值
        //对key进行md5加密    DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8))
        redisTemplate.opsForHash().put(id,DigestUtils.md5DigestAsHex(key.toString().getBytes(StandardCharsets.UTF_8)),value);

    }


    //缓存中取出数据
    @Override
    public Object getObject(Object key) {
        /*

         key = -368409926:4248786120:com.docker.dao.UserDao.selectAll:0:2147483647:select *  from user_info;:MybatisSqlSessionFactoryBean

         */
        System.out.println("key = " + DigestUtils.md5DigestAsHex(key.toString().getBytes(StandardCharsets.UTF_8)));
        RedisTemplate<Object,Object> redisTemplate = getRedisTemplate();
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //根据key 从redis的hash类型中获取数据
        return  redisTemplate.opsForHash().get(id, DigestUtils.md5DigestAsHex(key.toString().getBytes(StandardCharsets.UTF_8)));
        //return null;
    }

    /**
     * 删除缓存  mybatis没有实现
     * @param o key
     * @return result
     */
    @Override
    public Object removeObject(Object o) {
        log.info("删除数据，调用了 removeObject 方法");
        return null;
    }

    /**
     * 清空缓存  只要发生增删改动作，都会触发清空缓存
     */
    @Override
    public void clear() {
        log.info("删除数据，调用了clear方法");
        RedisTemplate<Object,Object> redisTemplate = getRedisTemplate();

        //情况缓存 将大key删除
        redisTemplate.delete(id);

    }

    /**
     * 用来计算缓存的击数量
     * @return 击中率
     */
    @Override
    public int getSize() {
        RedisTemplate<Object,Object> redisTemplate = getRedisTemplate();
        //获取hash 中缓存的数量
        return redisTemplate.opsForHash().size(id).intValue();
    }


    /**
     * redisTemplate 统一封装
     * @return redisTemplate
     */
    private static RedisTemplate<Object,Object> getRedisTemplate() {
        //首字母必须默认为小写
        RedisTemplate<Object,Object> redisTemplate = (RedisTemplate<Object,Object>)ApplicationContextUtils.getBean("redisTemplate");
        //redis 的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
