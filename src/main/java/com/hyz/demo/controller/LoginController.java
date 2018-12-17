package com.hyz.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: huangyazhe
 * Date: 2018/12/16
 * Time: 15:23
 */


@Controller
public class LoginController {

    @RequestMapping( "/login" )
    @ResponseBody
    public String login(@RequestParam String body){
        System.out.println(body);

        return "login is true ";
    }
}
