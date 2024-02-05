package com.docker.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @description:  开启应用session配置类
 * @author: shangqj
 * @date: 2024/2/5
 * @version: 1.0
 */
@Configuration
@EnableRedisHttpSession  //将整个应用中使用session的数据全部交给redis处理
public class RedisSessionManager {
}
