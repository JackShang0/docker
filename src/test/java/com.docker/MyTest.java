package com.docker;

import com.docker.bean.User;
import com.docker.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@SpringBootTest(classes = Appendable.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MyTest {

    @Autowired
    UserDao userDao;



    @Test
    public void myTest(){

        List<User> users = userDao.selectAll();
        log.info("users->{}",users);
    }

}
