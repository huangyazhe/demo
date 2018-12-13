package com.hyz.demo.controller;

import com.hyz.demo.service.UserService;
import com.hyz.trace.Trace;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public void check(@RequestParam String userId) throws ServletException, IOException {
        //String res1 = userService.getUserInfo(userId);
        //String res2 = userService.getUserName();
        //String age = UserAge();
        System.out.println("HiController res-------");


    }

    @RequestMapping( "/aa" )
    public String UserAge(String age){
        return "18 ";
    }


}
