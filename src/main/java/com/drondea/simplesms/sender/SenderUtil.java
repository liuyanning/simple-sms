package com.drondea.simplesms.sender;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.sender.service.AbstractTcpSenderService;
import com.drondea.simplesms.sender.service.CmppServiceImpl;
import com.drondea.simplesms.util.ApplicationContextUtil;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.message.IMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyanning
 */
public class SenderUtil {

    public static final Map<String, AbstractTcpSenderService> TCP_PROTOCOL_SERVICE_MAP = new HashMap<>(16);

    static {
        CmppServiceImpl cmppService = ApplicationContextUtil
                .getBean("cmppService");
        TCP_PROTOCOL_SERVICE_MAP.put(ProtocolType.CMPP.toString(), cmppService);
        TCP_PROTOCOL_SERVICE_MAP.put(ProtocolType.CMPP3.toString(), cmppService);
        TCP_PROTOCOL_SERVICE_MAP.put(ProtocolType.CMPP2.toString(), cmppService);
    }

    /**
     * 将submit转换成要提交的tcp协议对象
     * @param channel
     * @param submit
     * @param channelSession
     * @return
     */
    public static IMessage getTcpSendMsg(Channel channel, Submit submit, ChannelSession channelSession) {
        if (!channelSession.getChannel().isActive()) {
            return null;
        }
        AbstractTcpSenderService service = TCP_PROTOCOL_SERVICE_MAP.get(channel.getProtocolType().toLowerCase());
        return service.getSubmitMessage(channel, submit, channelSession);
    }
}
