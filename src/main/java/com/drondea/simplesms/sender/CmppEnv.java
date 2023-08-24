package com.drondea.simplesms.sender;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.sender.hanlder.CmppClientCustomHandler;
import com.drondea.sms.conf.ClientSocketConfig;
import com.drondea.sms.conf.cmpp.CmppClientSocketConfig;
import com.drondea.sms.session.AbstractClientSessionManager;
import com.drondea.sms.session.cmpp.CmppClientSessionManager;
import com.drondea.sms.type.CmppConstants;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 移动网关系统环境
 *
 * @author Administrator
 */
@Component("cmppEnv")
public class CmppEnv extends AbstractTcpEnv {

    @Override
    protected ClientSocketConfig createClientSocketConfig(Channel channel) {
        CmppClientSocketConfig socketConfig = new CmppClientSocketConfig(channel.getChannelNo(),
                10 * 1000, 16, channel.getIp(), channel.getPort());

        socketConfig.setCharset(Charset.forName("utf-8"));
        socketConfig.setGroupName("cmpp");
        socketConfig.setUserName(channel.getAccount());
        //密码RSA加密了，需要解密
        socketConfig.setPassword(channel.getPassword());
        socketConfig.setIdleTime(30);
        // 协议版本
        socketConfig.setVersion(NumberUtils.toShort(channel.getVersion(), CmppConstants.VERSION_20));
        return socketConfig;
    }

    @Override
    protected AbstractClientSessionManager createSessionManager(Channel channel, ClientSocketConfig socketConfig) {
        CmppClientCustomHandler cmppCustomHandler = new CmppClientCustomHandler(channel);
        CmppClientSessionManager sessionManager = new CmppClientSessionManager(socketConfig, cmppCustomHandler);
        return sessionManager;
    }

}
