package com.docker.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class Order implements Serializable {

    /**

     CREATE TABLE `order_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `order` varchar(200) DEFAULT NULL COMMENT '订单流水号',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

     */
    private Integer id;
    private String order;

}
