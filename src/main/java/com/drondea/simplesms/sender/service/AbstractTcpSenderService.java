package com.drondea.simplesms.sender.service;

import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.cache.SubmitCache;
import com.drondea.simplesms.enums.Charset;
import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.util.CopyUtil;
import com.drondea.simplesms.util.SMSUtil;
import com.drondea.sms.channel.ChannelSession;
import com.drondea.sms.common.util.SystemClock;
import com.drondea.sms.message.IMessage;
import com.drondea.sms.type.IMessageResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * 
 * 多少个日日夜夜熬过来，才总结出来的经验
 * 
 * @author volcano
 * @date 2019年11月13日下午4:59:49
 * @version V1.0
 */
public abstract class AbstractTcpSenderService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractTcpSenderService.class);
	/**
	 *
	 * @param submit
	 *  提交类
	 *  下游的msgId
	 * @param responseMsg
	 */
	public void doSubmitResponse(Channel channel, Submit submit, IMessage responseMsg) {
		Submit cloneSubmit = new Submit();
		CopyUtil.SUBMIT_COPIER.copy(submit, cloneSubmit, null);
		IMessage requestMsg = responseMsg.getRequestMessage();
		setRequestInfo(channel, cloneSubmit, requestMsg);
		boolean submitStatus = parseSubmitResponse(responseMsg, cloneSubmit);
		// 真实的提交时间
		cloneSubmit.setSubmitDate(new Date(requestMsg.getSendTimeStamp()));
		Date now = new Date();
		// 响应时间
		cloneSubmit.setSubmitResponseDate(now);

		if (submitStatus) {
			cloneSubmit.setSubmitStatus(true);
		} else {
			logger.error("提交失败：" + submit.toString());
			return;
		}
		String key = SMSUtil.getSubmitKey(channel.getChannelNo(), cloneSubmit.getChannelMsgId());
		//保存到本地缓存
		SubmitCache.saveSubmit(key, cloneSubmit);
	}

	public IMessage getSubmitMessage(Channel channel, Submit submit, ChannelSession channelSession) {
		Date now = new Date(SystemClock.now());
		// 这两个时间是初始时间，提交时间和创建时间会被重置
		submit.setSubmitDate(now);
		//发送器的端口
		InetSocketAddress socketAddress = (InetSocketAddress) channelSession.getChannel().localAddress();
		submit.setPort(socketAddress.getPort());

		//UTF-8编码要转换成UCS2
		if (Charset.UTF8.toString().equalsIgnoreCase(submit.getCharset())) {
			submit.setCharset(Charset.UCS2.toString());
		}
		IMessage requestMessage = wrapSubmitRequest(channelSession, channel, submit);

		requestMessage.setMessageResponseHandler(new IMessageResponseHandler() {

			@Override
			public void messageComplete(IMessage msgRequest, IMessage msgResponse) {
				System.out.println(msgResponse);
				msgResponse.setRequestMessage(msgRequest);
				//这里不使用线程池
				doSubmitResponse(channel, submit, msgResponse);
			}

			@Override
			public void messageExpired(String key, IMessage msgRequest) {
				//响应超时处理
			}

			@Override
			public void sendMessageFailed(IMessage msgRequest) {
				//发送失败处理
			}
		});
		return requestMessage;
	}

	/**
	 * 设置请求信息
	 * @param channel
	 * @param submitted
	 * @param request
	 */
	protected abstract void setRequestInfo(Channel channel, Submit submitted, IMessage request);


	/**
	 * 
	 * @param response
	 * @param submit
	 * @return
	 * @author volcano
	 * @date 2019年11月13日下午4:50:08
	 * @version V1.0
	 */
	public abstract boolean parseSubmitResponse(IMessage response, Submit submit);

	/**
	 * 把submit对象转换为各个协议对象
	 * @param channel
	 * @param submit
	 * @return
	 * @author volcano
	 * @date 2019年11月13日下午6:01:38
	 * @version V1.0
	 */
	protected abstract IMessage wrapSubmitRequest(ChannelSession channelSession, Channel channel,
												  Submit submit);

}
