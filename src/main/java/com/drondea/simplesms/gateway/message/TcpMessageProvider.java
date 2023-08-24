package com.drondea.simplesms.gateway.message;

import com.drondea.simplesms.bean.Report;
import com.drondea.simplesms.cache.WaitNotifyReportQueue;
import com.drondea.simplesms.gateway.service.AbstractTcpService;
import com.drondea.simplesms.gateway.service.CmppAPIServiceImpl;
import com.drondea.simplesms.util.ApplicationContextUtil;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.message.MessageProvider;
import com.drondea.sms.session.AbstractServerSession;
import com.drondea.sms.type.UserChannelConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V3.0.0
 * @description: 拉取消息的接口实现
 * @author: 刘彦宁
 * @date: 2020年12月17日10:21
 **/
public class TcpMessageProvider implements MessageProvider {

    protected AbstractTcpService tcpService;

    public TcpMessageProvider() {
        this.tcpService = ApplicationContextUtil.getBean(CmppAPIServiceImpl.class);
    }

    public String getUserId(ChannelSession channelSession) {
        AbstractServerSession serverSession = (AbstractServerSession) channelSession;
        UserChannelConfig userChannelConfig = serverSession.getUserChannelConfig();
        return userChannelConfig.getId();
    }

    @Override
    public List<IMessage> getTcpMessages(ChannelSession channelSession) {
        String userId = getUserId(channelSession);
        List<IMessage> requestMsg = new ArrayList<>();
        //获取回执消息
        Report report = WaitNotifyReportQueue.pullReport(userId);
        if (report == null) {
            return requestMsg;
        }
        //回执不需要处理长短信
        IMessage reportMessage = tcpService.getReportMessage(channelSession, report);
        requestMsg.add(reportMessage);
        ////获取MO消息,暂无实现
        return requestMsg;
    }

    @Override
    public void responseMessageMatchFailed(String key, IMessage response) {
    }
}
