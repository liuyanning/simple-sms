package com.drondea.simplesms.sender.service;

import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.util.SMSUtil;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.common.SequenceNumber;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.message.cmpp.CmppSubmitRequestMessage;
import com.drondea.sms.message.cmpp.CmppSubmitResponseMessage;
import com.drondea.sms.thirdparty.SmsDcs;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author liuyanning
 */
@Service("cmppService")
public class CmppServiceImpl extends AbstractTcpSenderService {

	@Override
	protected void setRequestInfo(Channel channel, Submit submit, IMessage request) {
		CmppSubmitRequestMessage requestMessage = (CmppSubmitRequestMessage) request;
		submit.setSequence((int) requestMessage.getPkNumber());
		submit.setContent(requestMessage.getMsgContent());
	}


	@Override
	public boolean parseSubmitResponse(IMessage response, Submit submit) {
		CmppSubmitResponseMessage srp = (CmppSubmitResponseMessage) response;
		//上游返回的msgId,唯一标识一个消息
		submit.setChannelMsgId(srp.getMsgId().toString());
		submit.setResponseCode(String.valueOf(srp.getResult()));
		if (0 == srp.getResult()) {
			return true;
		}
		return false;
	}

	@Override
	protected IMessage wrapSubmitRequest(ChannelSession channelSession, Channel channel,
										 Submit submit) {
		//拼接通道的码号
		String srcId = submit.getSubCode();
		CmppSubmitRequestMessage requestMessage = new CmppSubmitRequestMessage();
		SequenceNumber sequenceNumber = channelSession.getSequenceNumber();
		requestMessage.getHeader().setSequenceId(sequenceNumber.next());
		String content = submit.getContent();
		requestMessage.setMsgContent(content, new SmsDcs(SMSUtil.getGeneralDataCodingDcs(submit.getCharset())));

		String phoneNo = submit.getMobile();
		requestMessage.setDestUsrTl((short) 1);
		requestMessage.setDestTerminalId(new String[] { phoneNo });
		requestMessage.setSrcId(srcId);
		requestMessage.setServiceId(StringUtils.defaultIfEmpty(channel.getServiceId(), "0000000000"));
		requestMessage.setFeeUserType(ObjectUtils.defaultIfNull(channel.getFeeUserType(), (short)2));
		requestMessage.setFeeTerminalId(StringUtils.defaultIfEmpty(channel.getFeeTerminalId(), ""));
		requestMessage.setMsgSrc(StringUtils.defaultIfEmpty(channel.getMsgSrc(), ""));
		requestMessage.setRegisteredDelivery((short) 1);
		return requestMessage;
	}
}
