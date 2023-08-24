package com.drondea.simplesms.util;

import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;

/**
 * @version V3.0.0
 * @description: netty 发送数据
 * @author: 刘彦宁
 * @date: 2020年07月25日11:55
 **/
public class NettySender {

    /**
     * 放到netty中发送，在用户线程中发送有问题
     * @param channelSession
     * @param iMessage
     */
    public static void sendMessage(ChannelSession channelSession, IMessage iMessage) throws Exception {
        if (!channelSession.getChannel().isActive()) {
            throw new Exception("通道已关闭不能发送了");
        }
        channelSession.sendMessage(iMessage);
    }
}
