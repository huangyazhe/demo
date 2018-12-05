package com.hyz.demo.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 添加新老接口开关，对新上线业务可以随时降级处理
 * @author HuangYazhe
 * Date: 2018/12/4
 */
@Controller
public class CheckLiveController {

    private static final String downgradeSwitch = "1";
    @NacosInjected
    private ConfigService configService;

    @RequestMapping( "/getUserInfo" )
    public String getUserInfo() {

        String res  = "";

        try {

            String downgrade = configService("xinghuo.web.getUserinfo.key", "1");

            if (downgradeSwitch.equals(downgrade)) {

                res = "请注意！！！这是新的业务逻辑";

            }
            else {

                res = "这是老的业务逻辑";

            }
        }
         catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }


    @RequestMapping(value = "/publish", method = POST)
    @ResponseBody
    public boolean publish(@RequestParam String dataId, @RequestParam(defaultValue = DEFAULT_GROUP) String groupId,
            @RequestParam String content) throws NacosException {
        return configService.publishConfig(dataId, groupId, content);
    }



    @NacosConfigListener(
            dataId = "keyListener",
            timeout = 500
    )
    public void onChange(String newContent) throws Exception {
        Thread.sleep(100);
        System.out.println("onChange : " + newContent);
    }


    public String configService(String key, String baseline){
        String config = "";
        try {
            config = configService.getConfig("xinghuo.web.getUserinfo.key", "xinghuo-web", 1000);
            if(config == null){
                config = baseline;
            }

        } catch (NacosException e) {
            e.printStackTrace();
        }

        return config;
    }


   /**

    模版方法 WebAPI

    业务A  AHardler --> service --> rpc
    业务B  BHardler --> service --> rpc
    业务C  CBardler --> service --> rpc

    A.then(B)   //串行调用


    A.add()     //并行调用
    B.add()
            .then(c)





    */

}
