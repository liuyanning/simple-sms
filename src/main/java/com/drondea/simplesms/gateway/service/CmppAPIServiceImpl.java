package com.drondea.simplesms.gateway.service;

import com.drondea.simplesms.bean.Report;
import com.drondea.simplesms.cache.UserMsgIdCache;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.sender.SenderStarter;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.common.util.MsgId;
import com.drondea.sms.common.util.SystemClock;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.message.cmpp.CmppDeliverRequestMessage;
import com.drondea.sms.message.cmpp.CmppReportRequestMessage;
import com.drondea.sms.type.IMessageResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class CmppAPIServiceImpl extends AbstractTcpService {

    private static final Logger logger = LoggerFactory.getLogger(CmppAPIServiceImpl.class);

    @Override
    public IMessage getReportMessage(ChannelSession channelSession, Report entity) {
        // 写入状态
        CmppDeliverRequestMessage request = new CmppDeliverRequestMessage();
        request.setMsgId(new MsgId());
        request.getHeader().setSequenceId(channelSession.getSequenceNumber().next());
        String phoneNo = entity.getMobile().replaceFirst("\\+", "");
        // 源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）
        request.setSrcTerminalId(phoneNo);
        CmppReportRequestMessage reportRequestMessage = new CmppReportRequestMessage();
        reportRequestMessage.setDestterminalId(phoneNo);
        String msgId;
        if(entity.getTotal() == 1){
            msgId = entity.getMsgId();
        } else {
            msgId = UserMsgIdCache.getMsgId(entity.getBatchNo(), entity.getSequence());
        }
        if (StringUtils.isEmpty(msgId)) {
            return null;
        }
        reportRequestMessage.setMsgId(new MsgId(msgId));
        String submitTime = DateFormatUtils.format(entity.getSubmitDate(), "yyMMddHHmm");
        String t = DateFormatUtils.format(SystemClock.now(), "yyMMddHHmm");
        reportRequestMessage.setSubmitTime(submitTime);
        reportRequestMessage.setDoneTime(t);

        if ("success".equals(entity.getStatusCode())) {
            reportRequestMessage.setStat("DELIVRD");
        } else {
            reportRequestMessage.setStat(getNativeStatus(entity));
        }
        reportRequestMessage.setSmscSequence(0);
        request.setReportRequestMessage(reportRequestMessage);
        request.setRegisteredDelivery((short) 1);

        request.setDestId(entity.getInputSubCode());

        InetSocketAddress socketAddress = (InetSocketAddress) channelSession.getChannel().remoteAddress();
        int port = socketAddress.getPort();
        entity.setPort(port);
        request.setMessageResponseHandler(new IMessageResponseHandler() {
            @Override
            public void messageComplete(IMessage iMessage, IMessage iMessage1) {
                logger.info(iMessage1.toString());
            }

            @Override
            public void messageExpired(String s, IMessage iMessage) {

            }

            @Override
            public void sendMessageFailed(IMessage iMessage) {

            }
        });
        return request;
    }

    private String getNativeStatus(Report entity) {
        String nativeStatus = entity.getNativeStatus();
        Channel channel = SenderStarter.getChannelByNo(entity.getChannelNo());
        //非cmpp的通道错误码获取description中的状态，中文忽略
        if (channel != null
                && !ProtocolType.CMPP.equals(channel.getProtocolType())
                && StringUtils.isAsciiPrintable(entity.getDescription())) {
            nativeStatus = StringUtils.left(entity.getDescription(), 7);
        }
        return nativeStatus;
    }
}
