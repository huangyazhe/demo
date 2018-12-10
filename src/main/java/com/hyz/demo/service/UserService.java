package com.hyz.demo.service;

import com.hooke.trace.Trace;
import org.springframework.stereotype.Component;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/10
 */
@Component("traceTest")
public class UserService {
    @Trace(processName = "test",startStep = true)
    public String getUserInfo(){
        System.out.println("1111111");
        return "11111111";
    }

}
