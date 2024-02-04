package com.docker.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/4
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class User {

    /**
     *   `id` bigint NOT NULL AUTO_INCREMENT,
     *   `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
     *   `age` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
     *   `class` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
     *   `subject` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
     *   PRIMARY KEY (`id`)
     */
    private Integer id;
    private String name;
    private String age;
    private String className;
    private String subject;

}
