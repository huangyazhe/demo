package com.hyz.demo.rpc;

import com.hyz.demo.domain.UserInfo;
import org.springframework.stereotype.Service;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/10
 */
@Service
public class UserRpc {

    public UserInfo getUserInfo(String userId){
        UserInfo userInfo = new UserInfo();
        userInfo.setAge("18");
        userInfo.setName("hyz");
        return userInfo;
    }
}
