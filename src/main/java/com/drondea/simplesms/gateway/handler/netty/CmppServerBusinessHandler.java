package com.drondea.simplesms.gateway.handler.netty;

import com.drondea.simplesms.bean.Input;
import com.drondea.simplesms.cache.UserMsgIdCache;
import com.drondea.simplesms.cache.WaitSendQueue;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.util.NettySender;
import com.drondea.simplesms.util.SMSUtil;
import com.drondea.sms.common.util.MsgId;
import com.drondea.sms.common.util.SystemClock;
import com.drondea.sms.message.cmpp.CmppSubmitRequestMessage;
import com.drondea.sms.message.cmpp.CmppSubmitResponseMessage;
import com.drondea.sms.session.cmpp.CmppServerSession;
import com.drondea.sms.type.CmppConstants;
import com.drondea.sms.type.UserChannelConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * @version V3.0.0
 * @description: cmpp服务器端接收到消息业务处理
 * @author: 刘彦宁
 * @date: 2020年07月28日11:06
 **/
public class CmppServerBusinessHandler extends ChannelInboundHandlerAdapter {

    private final CmppServerSession cmppServerSession;

    public CmppServerBusinessHandler(CmppServerSession cmppServerSession) {
        this.cmppServerSession = cmppServerSession;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CmppSubmitRequestMessage) {
            CmppSubmitRequestMessage requestMessage = (CmppSubmitRequestMessage) msg;
            CmppSubmitResponseMessage responseMessage = new CmppSubmitResponseMessage(requestMessage.getHeader());
            MsgId responseMsgId = new MsgId();
            //msgid 响应，回执要用
            responseMessage.setMsgId(responseMsgId);
            //响应response
            NettySender.sendMessage(cmppServerSession, responseMessage);
            String batchNumber = requestMessage.getBatchNumber();
            short pkNumber = requestMessage.getPkNumber();
            String msgId = responseMsgId.toString();
            //长短信组装不完整返回
            if (requestMessage.isLongMsg()) {
                //长短信缓存msgid
                UserMsgIdCache.saveMsgId(batchNumber, pkNumber, msgId);
                if (!requestMessage.isMsgComplete()) {
                    return;
                }
            }
            Input input = new Input();
            input.setBatchNo(batchNumber);
            //用户提交时间
            long timestamp = SystemClock.now();
            input.setTimestamp(timestamp);
            UserChannelConfig userChannelConfig = cmppServerSession.getUserChannelConfig();
            String userId = userChannelConfig.getId();
            input.setUserId(userId);
            //消息处理
            input.setMsgId(msgId);
            String[] mobiles = requestMessage.getDestTerminalId();
            input.setMobile(mobiles[0]);
            String subCode = requestMessage.getSrcId();
            input.setSubCode(subCode);
            String charset = SMSUtil.getCharsetByByte(requestMessage.getMsgFmt().getValue());
            input.setCharset(charset);
            InetSocketAddress socket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIp = socket.getAddress().getHostAddress();
            input.setClientIp(clientIp);
            int port = socket.getPort();
            input.setPort(port);
            short version = userChannelConfig.getVersion();
            ProtocolType protocolType = getCmppVersion(version);
            input.setProtocolType(protocolType.toString());
            String content = requestMessage.getMsgContent();
            input.setContent(content);
            //长短信总条数
            short pkTotal = requestMessage.getPkTotal();
            input.setTotal((int) pkTotal);
            System.out.println("收到消息：" + content);
            WaitSendQueue.route(input);
        }
        super.channelRead(ctx, msg);
    }

    private ProtocolType getCmppVersion(short version){
        if (version < CmppConstants.VERSION_30) {
            return ProtocolType.CMPP2;
        }
        return ProtocolType.CMPP3;
    }
}
