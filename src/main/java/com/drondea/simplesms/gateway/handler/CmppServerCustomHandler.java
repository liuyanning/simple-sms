package com.drondea.simplesms.gateway.handler;

import com.drondea.simplesms.gateway.handler.netty.CmppServerBusinessHandler;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.session.cmpp.CmppServerSession;
import io.netty.channel.ChannelPipeline;


/**
 * @version V3.0.0
 * @description: cmpp的定制处理器
 * @author: 刘彦宁
 * @date: 2020年06月23日17:35
 **/
public class CmppServerCustomHandler extends AbstractServerCustomHandler {

    @Override
    public void configPipelineAfterLogin(ChannelPipeline pipeline, ChannelSession channelSession) {
        pipeline.addLast("CmppServerBusinessHandler", new CmppServerBusinessHandler((CmppServerSession) channelSession));
    }
}
