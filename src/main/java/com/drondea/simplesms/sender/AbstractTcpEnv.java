package com.drondea.simplesms.sender;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.sender.message.SenderTcpMessageProvider;
import com.drondea.sms.conf.ClientSocketConfig;
import com.drondea.sms.session.AbstractClientSessionManager;
import com.drondea.sms.session.sgip.SgipClientSessionManager;
import com.drondea.sms.type.SignatureDirection;
import com.drondea.sms.type.SignaturePosition;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @version V3.0.0
 * @description: tcp协议的客户端创建
 * @author: 刘彦宁
 * @date: 2021年02月18日16:48
 **/
abstract class AbstractTcpEnv {

    /**
     * 创建ClientSocketConfig配置对象
     * @param channel
     * @return
     */
    protected abstract ClientSocketConfig createClientSocketConfig(Channel channel);

    /**
     * 创建ClientSessionManager对象
     * @param channel
     * @param socketConfig
     * @return
     */
    protected abstract AbstractClientSessionManager createSessionManager(Channel channel, ClientSocketConfig socketConfig);

    /**
     * tcp的共有参数
     * @param socketConfig
     * @param channel
     */
    private void setClientSocketConfig(ClientSocketConfig socketConfig, Channel channel) {
        socketConfig.setChannelSize(channel.getChannelSize() == 0 ? 1 : channel.getChannelSize());
        socketConfig.setCountersEnabled(true);
        socketConfig.setWindowMonitorInterval(10 * 1000);
        //设置响应超时时间
        socketConfig.setRequestExpiryTimeout(30 * 1000);
        //发送限速
        Integer submitSpeed = ObjectUtils.defaultIfNull(channel.getSpeed(), 200);
        socketConfig.setQpsLimit(submitSpeed);
        //签名方向
        socketConfig.setSignatureDirection(SignatureDirection.CUSTOM);
        //签名位置
        socketConfig.setSignaturePosition(SignaturePosition.PREFIX);
    }

    public AbstractClientSessionManager createClient(Channel channel) {
        ClientSocketConfig socketConfig = createClientSocketConfig(channel);
        //设置公共参数
        setClientSocketConfig(socketConfig, channel);
        AbstractClientSessionManager sessionManager = createSessionManager(channel, socketConfig);
        sessionManager.setMessageProvider(new SenderTcpMessageProvider(channel));
        //sgip不需要创建连接，由业务控制有消息的时候创建
        if (sessionManager instanceof SgipClientSessionManager) {
            return sessionManager;
        }
        sessionManager.doOpen();
        //启动定时任务
        sessionManager.doCheckSessions();
        return sessionManager;
    }
}
