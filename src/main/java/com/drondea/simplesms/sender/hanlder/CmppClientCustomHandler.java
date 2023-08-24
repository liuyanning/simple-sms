package com.drondea.simplesms.sender.hanlder;

import com.drondea.simplesms.sender.hanlder.netty.CmppClientBusinessHandler;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.session.cmpp.CmppClientSession;
import com.drondea.sms.type.ICustomHandler;
import com.drondea.sms.type.UserChannelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version V3.0.0
 * @description: cmpp的定制处理器
 * @author: 刘彦宁
 * @date: 2020年06月23日17:35
 **/
public class CmppClientCustomHandler extends ICustomHandler {

    private static final Logger logger = LoggerFactory.getLogger(CmppClientCustomHandler.class);

    private com.drondea.simplesms.bean.Channel bizChannel;

    public CmppClientCustomHandler(com.drondea.simplesms.bean.Channel bizChannel) {
        this.bizChannel = bizChannel;
    }

    @Override
    public void fireUserLogin(Channel channel, ChannelSession channelSession) {
        logger.debug("cmpp通道【{}】登录成功--开始发送短信", bizChannel.getAccount());
    }

    @Override
    public void channelClosed(ChannelSession channelSession) {
        //发送器的端口
        logger.info(bizChannel.getAccount() + "连接断开");
    }

    @Override
    public void configPipelineAfterLogin(ChannelPipeline pipeline, ChannelSession channelSession) {
        //cmpp的回执和上行短信处理
        pipeline.addLast("cmppClientBusinessHandler", new CmppClientBusinessHandler((CmppClientSession) channelSession));
    }

    @Override
    public void responseMessageExpired(Integer sequenceId, IMessage request) {
        logger.debug("短信超时处理 {}", sequenceId);
    }

    @Override
    public void slidingWindowException(ChannelSession session, ChannelHandlerContext ctx, IMessage message, ChannelPromise promise, Exception exception) {
        logger.error("slidingWindowException", exception);
    }

    @Override
    public boolean customLoginValidate(IMessage message, UserChannelConfig channelConfig, Channel channel) {
        return true;
    }

}
