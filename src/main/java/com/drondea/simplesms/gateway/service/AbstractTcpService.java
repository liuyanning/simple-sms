package com.drondea.simplesms.gateway.service;

import com.drondea.simplesms.bean.Report;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;

/**
 * 默认实现 需要和 Sender工程协调 MQ
 *
 * @author zly
 */
public abstract class AbstractTcpService {

    /**
     * 获取用户的回执推送信息
     * @param channelSession
     * @param entity
     * @return
     */
    public abstract IMessage getReportMessage(ChannelSession channelSession, Report entity);
}
