package com.docker.aop.controller;

import com.docker.aop.MyCache;
import com.docker.aop.MyCacheEvict;
import com.docker.bean.Order;
import com.docker.dao.OrderDao;
import com.docker.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Resource
    OrderDao orderDao;

    //测试删除缓存
    @PostMapping("/deleteOrder")
    @MyCacheEvict(cacheNames = "Order", key = "selectOrder")
    public String deleteCache(){
        System.out.println("deleteCache success");
        orderDao.deleteById("1");
        return "deleteCache success";
    }

    //测试新增缓存
    @PostMapping("/selectOrder")
    @MyCache(cacheNames = "Order", key = "selectOrder")
    public List<Order> selectOrder(){
        System.out.println("addCache success");
        return  orderDao.selectAll();
    }


    @PostMapping("/selectOrderById")
    @MyCache(cacheNames = "Order", key = "selectOrder")
    public Order selectOrderById(@RequestBody Order order){
        return orderDao.selectOrderById(order);
    }
}