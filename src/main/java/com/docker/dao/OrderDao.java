package com.docker.dao;

import com.docker.bean.Order;
import com.docker.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@Mapper
public interface OrderDao {

    List<Order> selectAll();
    Order selectById(String id);
    void deleteById(String id);

    int saveUser(User user);
}
