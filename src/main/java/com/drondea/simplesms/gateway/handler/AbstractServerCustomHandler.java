package com.drondea.simplesms.gateway.handler;

import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.session.AbstractServerSession;
import com.drondea.sms.session.AbstractServerSessionManager;
import com.drondea.sms.type.ICustomHandler;
import com.drondea.sms.type.UserChannelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version V3.0.0
 * @description: netty服务端抽象处理
 * @author: 刘彦宁
 * @date: 2021年03月08日14:27
 **/
public abstract class AbstractServerCustomHandler extends ICustomHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServerCustomHandler.class);

    @Override
    public void fireUserLogin(Channel channel, ChannelSession channelSession) {
        AbstractServerSession serverSession = (AbstractServerSession) channelSession;
        UserChannelConfig userChannelConfig = serverSession.getUserChannelConfig();
        int id = Integer.parseInt(userChannelConfig.getId());
        logger.info("用户{}登录了", id);
    }

    @Override
    public void channelClosed(ChannelSession channelSession) {
        AbstractServerSession serverSession = (AbstractServerSession) channelSession;
        UserChannelConfig userChannelConfig = serverSession.getUserChannelConfig();
        if (userChannelConfig == null) {
            return;
        }
        AbstractServerSessionManager sessionManager = (AbstractServerSessionManager) channelSession.getSessionManager();
        logger.info("用户{}断开连接", userChannelConfig.getUserName());
    }

    @Override
    public void responseMessageExpired(Integer sequenceId, IMessage request) {
        logger.error("短信超时处理 {}", sequenceId);
    }

    @Override
    public void slidingWindowException(ChannelSession session, ChannelHandlerContext ctx, IMessage message, ChannelPromise promise, Exception exception) {
        logger.error("slidingWindowException", exception);
    }

    @Override
    public boolean customLoginValidate(IMessage message, UserChannelConfig channelConfig, Channel channel) {
        return true;
    }

    @Override
    public void failedLogin(ChannelSession channelSession, IMessage iMessage, long status) {
        AbstractServerSession serverSession = (AbstractServerSession) channelSession;
        UserChannelConfig userChannelConfig = serverSession.getUserChannelConfig();
        if (userChannelConfig == null) {
            return;
        }
        logger.info("用户{}登录失败，返回值：{}", userChannelConfig.getUserName(), status);
    }
}
