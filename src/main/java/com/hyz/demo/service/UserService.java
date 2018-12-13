package com.hyz.demo.service;

import com.hyz.demo.domain.UserInfo;
import com.hyz.demo.rpc.UserRpc;
import com.hyz.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/10
 */
@Component("traceTest")
public class UserService {

    @Autowired
    private UserRpc userRpc;


    public String getUserInfo(String userId){
        UserService us = new UserService();
        //UserInfo userInfo = userRpc.getUserInfo(userId);

        String name = us.getUserName();
        return "";
    }

    //@Trace(processName = "UserName",startStep = true)
    public String getUserName(){
        return "1+2";
    }

}
