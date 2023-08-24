package com.drondea.simplesms.sender.hanlder.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @version V3.0.0
 * @description: 客户端业务处理
 * @author: 刘彦宁
 * @date: 2020年07月24日09:26
 **/
public class AbstractClientBusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
