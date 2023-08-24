package com.drondea.simplesms.sender.hanlder.netty;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.sender.ReportWrap;
import com.drondea.simplesms.sender.SenderStarter;
import com.drondea.simplesms.util.NettySender;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.cmpp.CmppDeliverRequestMessage;
import com.drondea.sms.message.cmpp.CmppDeliverResponseMessage;
import com.drondea.sms.session.cmpp.CmppClientSession;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @version V3.0.0
 * @description: cmpp的客户端收到回执消息和MO业务处理
 * @author: 刘彦宁
 * @date: 2020年07月24日09:57
 **/
public class CmppClientBusinessHandler extends AbstractClientBusinessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CmppClientBusinessHandler.class);

    private final CmppClientSession cmppClientSession;
    public CmppClientBusinessHandler(CmppClientSession cmppClientSession) {
        this.cmppClientSession = cmppClientSession;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CmppDeliverRequestMessage) {
            CmppDeliverRequestMessage dp = (CmppDeliverRequestMessage) msg;
            try {
                // 状态报告
                if (dp.isReport()) {
                    doReport(cmppClientSession, dp);
                } else {
                    doMo(cmppClientSession, dp);
                }
            } catch (Exception e) {
                logger.error("deliver 出错了：" + e.getMessage(),e);
            }
            responseDeliver(cmppClientSession, dp);
            return;
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 响应回执
     *
     * @param channelSession
     * @param dp
     */
    protected void responseDeliver(ChannelSession channelSession, CmppDeliverRequestMessage dp) throws Exception {
        CmppDeliverResponseMessage responseMessage = new CmppDeliverResponseMessage(dp.getHeader());
        responseMessage.setResult(0);
        responseMessage.setMsgId(dp.getMsgId());
        NettySender.sendMessage(channelSession, responseMessage);
    }

    private void doMo(ChannelSession channelSession, CmppDeliverRequestMessage dp) {
        String channelNo = channelSession.getConfiguration().getId();
        InetSocketAddress socketAddress = (InetSocketAddress) channelSession.getChannel().localAddress();
        int port = socketAddress.getPort();
        //组装不完整返回
        if (!dp.isMsgComplete()) {
            return;
        }
        //打印收到的MO，不处理
    }

    /**
     * 长短信也会有多条状态报告
     *
     * @param dp
     */
    private void doReport(ChannelSession channelSession, CmppDeliverRequestMessage dp) {
        String id = channelSession.getConfiguration().getId();
        Channel channel = SenderStarter.getChannelByNo(id);
        InetSocketAddress socketAddress = (InetSocketAddress) channelSession.getChannel().localAddress();
        ReportWrap reportWrap = new ReportWrap(dp, channel);
        reportWrap.setPort(socketAddress.getPort());
        reportWrap.setLocalIp("127.0.0.1");
        reportWrap.doReportBiz();
    }
}
