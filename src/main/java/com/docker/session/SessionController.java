package com.docker.session;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: shangqj
 * @date: 2024/2/5
 * @version: 1.0
 */
@RestController
public class SessionController {


    /**
     * 测试 Session 是否共享
     * @param request 请求
     * @param response 响应
     * @throws IOException  e
     */
    @PostMapping("/sessionTest")
    public void sessionTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> list = (List<String>) request.getSession().getAttribute("list");

        if (list==null)
            list = new ArrayList<>();

        list.add("xxx");

        //使用Redis管理Session时，必须将Session中变化的数据同步到Redis中
        request.getSession().setAttribute("list",list);
        response.getWriter().println("size "+list.size());
        response.getWriter().println("sessionId"+request.getSession().getId());
    }
}
