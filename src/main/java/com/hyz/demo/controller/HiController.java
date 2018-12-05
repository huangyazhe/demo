package com.hyz.demo.controller;

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
 */
@Controller
public class HiController  {
    @RequestMapping( "/hi" )
    public void check( HttpServletRequest req ,HttpServletResponse resp) throws ServletException, IOException {
        String config = null;
        System.out.println("hyz-------");

    }


}
