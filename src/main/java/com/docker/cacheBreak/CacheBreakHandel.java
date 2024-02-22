package com.docker.cacheBreak;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.docker.aop.MyCache;
import com.docker.bean.Order;
import com.docker.bean.User;
import com.docker.dao.OrderDao;
import com.docker.dao.UserDao;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/22
 * @version: 1.0
 */
@RestController
@Slf4j
public class CacheBreakHandel {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate<String, Serializable> redisTemplate;
    @Resource
    OrderDao orderDao;
    @Resource
    UserDao userDao;


    @PostMapping("/selectOrderById2")
    @MyCache(cacheNames = "Order", key = "selectOrder")
    public Order selectOrderById(@RequestBody Order order){
        return orderDao.selectOrderById(order);
    }

    @PostMapping("/selectUserById2")
    public User selectUserById(@RequestBody User user0){
        String id = String.valueOf(user0.getId());
        User user = userDao.selectById("1");
        System.out.println("user = " + user);
        System.out.println(" ==================== " );
        //return userDao.selectById("1");
        return queryWithMutex(id);
    }

    /**
     * 互斥锁解决 缓存击穿问题  热点key问题
     * @param id id
     * @return 数据结果
     */
    public User queryWithMutex(String id){
        String key = "cache"+ id;
        //1.从redis中查询用户信息
        String s = stringRedisTemplate.opsForValue().get(key);

        //2.判断是否存在/是否为空值
        if (!StringUtils.isNullOrEmpty(s)){
            return JSONUtil.toBean(s,User.class);
        }

        //3.不存在，实现缓存的重建
        //3.1 获取互斥锁
        String lockKey = "cache:user"+id;
        boolean b = tryLock(lockKey);
        //      获取互斥锁是否成功：
        User user = null;
        try {
            if (!b){
                //失败，休眠一段时间，再次获取锁
                Thread.sleep(50);
                //重试 此处做递归即可
                return queryWithMutex(id);
            }

            //成功，先从缓存中查询是否存在，不存在，则根据id查询数据，将数据存入redis中
            String s1 = stringRedisTemplate.opsForValue().get(key);
            if (!StringUtils.isNullOrEmpty(s1)){
                return JSONUtil.toBean(s1,User.class);
            }
            //不存在，则查询数据库
            user = userDao.selectById(id);
            TimeUnit.MILLISECONDS.sleep(200);
            if (user == null){
                return new User();
            }else {
                //存入缓存
                stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(user),1000,TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放锁
            delLock(lockKey);
        }

        return user;

    }


    /**
     * 针对缓存击穿情况下  缓存重置过程中的处理方案 ：互斥锁
     * 获取锁
     * @param key 键
     * @return 是否获取到锁
     */
    private boolean tryLock(String key){
        //setIfAbsent = setnx 即使用互斥锁进行缓存重置  todo 未指定过期时间
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
        //使用 hutool 的布尔工具类，防止装箱、拆箱空指针问题
        return BooleanUtil.isTrue(aBoolean);
    }


    /**
     * 删除锁
     * @param key key
     */
    private void delLock(String key){
        //删除缓存
        stringRedisTemplate.delete(key);
    }
}
