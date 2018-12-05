package com.hyz.demo.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.spring.context.annotation.EnableNacos;
import org.springframework.context.annotation.Configuration;

/**
 * HI 雅哲，别忘记写注释哦，认真是一种态度
 * @author HuangYazhe
 * Date: 2018/12/3
 */

@Configuration
@EnableNacos(
        globalProperties =
        @NacosProperties(serverAddr = "${nacos.server-addr:localhost:8848}")
)
public class NacosConfiguration {
    @NacosInjected
    private ConfigService configService;
}
