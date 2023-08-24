package com.drondea.simplesms;

import com.drondea.simplesms.gateway.CmppServerEnv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author liuyanning
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class SimpleSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSmsApplication.class, args);
        //启动cmppServer用于接收用户短信
        CmppServerEnv.start();
        //发送器启动改为bean初始化启动
//        SenderStarter.start();
    }

}
