package com.drondea.simplesms.sender.message;

import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.cache.WaitSendQueue;
import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.sender.SenderUtil;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.common.SequenceNumber;
import com.drondea.sms.common.util.CommonUtil;
import com.drondea.sms.message.ILongSMSMessage;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.message.MessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @version V3.0.0
 * @description: 提供发送包的接口
 * @author: 刘彦宁
 * @date: 2020年11月18日12:18
 **/
public class SenderTcpMessageProvider implements MessageProvider {

    private static final Logger logger = LoggerFactory.getLogger(SenderTcpMessageProvider.class);

    /**
     * 通道对象
     */
    private Channel channel;

    public SenderTcpMessageProvider(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public List<IMessage> getTcpMessages(ChannelSession channelSession) {
        return pullMessageFromCache(channelSession);
    }

    @Override
    public void responseMessageMatchFailed(String requestKey, IMessage response) {
        logger.info("submit response not matched {}, {}", requestKey, response);
    }

    /**
     * 返回是否存在消息,此处是立即返回，回调是异步执行
     *
     * @return
     */
    public List<IMessage> pullMessageFromCache(ChannelSession channelSession) {
        Channel channel = getChannel();
        Submit submit = WaitSendQueue.pullSubmit(channel.getOperator());
        if (submit == null) {
            return null;
        }
        //码号处理，拼接主码号
        submit.setSubCode(channel.getSpNumber() + submit.getSubCode());
        submit.setChannelNo(channel.getChannelNo());
        return getSubmitMessage(channelSession, submit);
    }

    /**
     * 将submit转成协议需要的对象，并拆分长短信
     * @param channelSession
     * @param submit
     * @return
     */
    public List<IMessage> getSubmitMessage(ChannelSession channelSession, Submit submit) {
        if (!channelSession.getChannel().isActive()) {
            return null;
        }
        //根据submit对象获取要提交的短信
        IMessage submitRequest = SenderUtil.getTcpSendMsg(getChannel(), submit, channelSession);
        if (submitRequest == null) {
            return null;
        }
        try {
            SequenceNumber sequenceNumber = channelSession.getSequenceNumber();
            List<IMessage> longMsgToEntity = CommonUtil.getLongMsgSlices((ILongSMSMessage) submitRequest,
                    channelSession.getConfiguration(), sequenceNumber);
            return longMsgToEntity;
        } catch (Exception e) {
            logger.error("pullMessageFromTopic" , e);
        }
        return null;
    }

    @Override
    public boolean isNoMessageContinue() {
        return true;
    }
}
