package com.hyz.demo.controller;

import com.hooke.trace.Trace;
import com.hyz.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 测试
 * @author HuangYazhe
 * Date: 2018/12/4
 *
 */
@Controller
public class HiController  {

    @Autowired
    private UserService userService;

    @Trace(processName = "check",startStep = true)
    @RequestMapping( "/hi" )
    public void check( HttpServletRequest req ,HttpServletResponse resp) throws ServletException, IOException {
        String res = userService.getUserInfo();
        System.out.println("HiController res-------"+res);

    }


}
