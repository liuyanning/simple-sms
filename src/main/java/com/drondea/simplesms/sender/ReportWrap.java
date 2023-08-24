package com.drondea.simplesms.sender;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.bean.Report;
import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.cache.SubmitCache;
import com.drondea.simplesms.cache.WaitNotifyReportQueue;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.util.SMSUtil;
import com.drondea.sms.message.cmpp.CmppDeliverRequestMessage;
import com.drondea.sms.message.cmpp.CmppReportRequestMessage;
import com.drondea.sms.message.sgip12.SgipReportRequestMessage;
import com.drondea.sms.message.smgp30.msg.SmgpDeliverRequestMessage;
import com.drondea.sms.message.smgp30.msg.SmgpReportMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @version V3.0.0
 * @description: 回执报告的包装类型
 * @author: 刘彦宁
 * @date: 2020年07月24日14:29
 **/
public class ReportWrap {

    private String msgId;
    private String phoneNo;
    private String statusCode;
    private String nativeStatus;
    private String description;
    private String spNumber;
    private Date statusDate;
    private String channelNo;
    private String protocolTypeCode;
    private String doneTime;
    private String submitTime;
    private Integer channelId;
    private Channel channel;
    private Integer port;
    private String localIp;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getNativeStatus() {
        return nativeStatus;
    }

    public void setNativeStatus(String nativeStatus) {
        this.nativeStatus = nativeStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(String spNumber) {
        this.spNumber = spNumber;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public ReportWrap() {
        super();
    }

    public ReportWrap(CmppDeliverRequestMessage dp, Channel channel) {
        this.setChannel(channel);
        CmppReportRequestMessage reportMessage = dp.getReportRequestMessage();
        String result = reportMessage.getStat();
        this.setMsgId(reportMessage.getMsgId().toString());
        this.setNativeStatus(result);
        if ("DELIVRD".equalsIgnoreCase(result)) {
            this.setStatusCode("success");
        } else {
            this.setStatusCode("failed");
        }
        this.setSpNumber(dp.getDestId());
        this.setProtocolTypeCode(ProtocolType.CMPP.toString());
        this.setSubmitTime(reportMessage.getSubmitTime());
        this.setDoneTime(reportMessage.getDoneTime());
        this.setPhoneNo(reportMessage.getDestterminalId());
        this.setChannelNo(channel.getChannelNo());
        this.setStatusDate(new Date());
        //清空description
        this.setDescription("");
    }

    public ReportWrap(SgipReportRequestMessage dp, Channel channel) {
        this.setChannel(channel);
        this.setMsgId(dp.getSubmitSequenceNumber().toString());
        //长度不能超7个字符
        this.setNativeStatus(getSgipNativeStatus(dp.getState(), dp.getErrorCode()));
        if (dp.getState() == 0 && dp.getErrorCode() == 0) {
            this.setStatusCode("success");
        } else {
            this.setStatusCode("failed");
        }
        //errorcode 借助description存储
        this.setDescription(String.valueOf(dp.getErrorCode()));
        this.setProtocolTypeCode(ProtocolType.SGIP.toString());
        this.setPhoneNo(dp.getUserNumber());
        this.setChannelNo(channel.getChannelNo());
        this.setStatusDate(new Date());
    }

    private String getSgipNativeStatus(short state, short errorCode) {
        StringBuilder builder = new StringBuilder();
        builder.append(state);
        builder.append(":");
        builder.append(errorCode);
        return builder.toString();
    }

    public ReportWrap(SmgpDeliverRequestMessage dp, Channel channel) {
        this.setChannel(channel);
        SmgpReportMessage reportMessage = dp.getReport();
        String result = reportMessage.getStat();
        String err = reportMessage.getErr();
        this.setMsgId(reportMessage.getSmgpMsgId().toString());
        this.setNativeStatus(result);
        if ("DELIVRD".equalsIgnoreCase(result)) {
            this.setStatusCode("success");
        } else {
            this.setStatusCode("failed");
        }
        this.setSpNumber(dp.getDestTermId());
        this.setDescription(err);//描述字段存放 Err错误代码表 6.2.63.2
        this.setProtocolTypeCode(ProtocolType.SMGP.toString());
        this.setSubmitTime(reportMessage.getSubTime());
        this.setDoneTime(reportMessage.getDoneTime());
        this.setPhoneNo(dp.getSrcTermId());
        this.setChannelNo(channel.getChannelNo());
        this.setStatusDate(new Date());
    }

    public void doReportBiz() {
        String key = SMSUtil.getSubmitKey(channel.getChannelNo(), getMsgId());
        Submit submit = SubmitCache.getSubmit(key);
        if (submit == null) {
            return;
        }
        //删除缓存
        SubmitCache.removeSubmit(key);
        buildReportAndNotify(submit);
    }

    public void buildReportAndNotify(Submit submit) {
        Report report = SMSUtil.buildReport(submit, submit.getMobile());
        report.setPort(this.getPort());
        report.setNativeStatus(this.nativeStatus);
        report.setStatusCode(this.statusCode);
        if (StringUtils.isNotEmpty(this.spNumber)) {
            report.setSpNumber(this.spNumber);
        }
        report.setDescription(this.description);
        //发送回执通知
        WaitNotifyReportQueue.offerReport(report);
    }


    @Override
    public String toString() {
        return "ReportWrap [msgId=" + msgId + ", phoneNo=" + phoneNo + ", statusCode=" + statusCode
                + ", nativeStatus=" + nativeStatus + ", description=" + description + ", spNumber=" + spNumber
                + ", statusDate=" + statusDate + ", channelNo=" + channelNo
                + ", protocolTypeCode=" + protocolTypeCode + "]";
    }
}
