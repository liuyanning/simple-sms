package com.drondea.simplesms.gateway.service;

import com.drondea.simplesms.bean.User;
import com.drondea.simplesms.config.UserConfigure;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.util.ApplicationContextUtil;
import com.drondea.sms.type.IValidator;
import com.drondea.sms.type.UserChannelConfig;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V3.0.0
 * @description: 网关抽象父类
 * @author: 刘彦宁
 * @date: 2021年02月20日10:20
 **/
public class TcpValidator implements IValidator {

    private static final Map<String, User> USER_MAP = new HashMap<>();

    static {
        UserConfigure userConfigure = ApplicationContextUtil.getBean(UserConfigure.class);
        List<User> users = userConfigure.getUsers();
        for (User user : users) {
            USER_MAP.put(user.getUserName(), user);
        }
    }

    private ProtocolType protocolType;
    public TcpValidator(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public UserChannelConfig getUserChannelConfig(String userName) {
        User user = getUser(userName);
        if (user == null) {
            return null;
        }
        UserChannelConfig userChannelConfig = new UserChannelConfig();
        userChannelConfig.setUserName(userName);
        userChannelConfig.setPassword(user.getPassword());
        userChannelConfig.setQpsLimit(ObjectUtils.defaultIfNull(user.getQpsLimit(), 200));
        userChannelConfig.setId(userName);
        //用户连接数限制
        userChannelConfig.setChannelLimit(ObjectUtils.defaultIfNull(user.getChannelLimit(), 200));
        //获取windowsize,设置推送MO和Report的窗口大小
        userChannelConfig.setWindowSize(64);
        userChannelConfig.setWindowMonitorInterval(10 * 1000);
        userChannelConfig.setRequestExpiryTimeout(30 * 1000);
        //获取心跳时间，默认30（秒）
        userChannelConfig.setIdleTime(30);
        //拉取
        return userChannelConfig;
    }

    public static User getUser(String userName) {
        return USER_MAP.get(userName);
    }
}
