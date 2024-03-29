package com.docker.dao;

import com.docker.bean.Student;
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
public interface StudentDao {

    List<Student> selectAll();
}
