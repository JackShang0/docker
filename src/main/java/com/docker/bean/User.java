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
public class User implements Serializable {

    /**

     CREATE TABLE `user_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `name` varchar(255) DEFAULT NULL,
     `age` varchar(255) DEFAULT NULL,
     `className` varchar(255) DEFAULT NULL,
     `subject` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

     */
    private Integer id;
    private String name;
    private String age;
    private String className;
    private String subject;

}
