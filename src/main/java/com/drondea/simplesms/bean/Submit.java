package com.drondea.simplesms.bean;

import java.util.Date;

/**
 * 下游提交的短信
 * @author liuyanning
 */
public class Submit {
    private String batchNo;
    private String userId;
    private String msgId;
    private String mobile;
    private String content;
    private String subCode;
    private String inputSubCode;
    private String charset;
    private String clientIp;
    private Integer port;
    private Integer total;
    private Integer sequence;
    private String protocolType;
    private Long timestamp;
    private String channelMsgId;
    private Date submitDate;
    private Date submitResponseDate;
    private String responseCode;
    private boolean submitStatus;
    private String channelNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Date getSubmitResponseDate() {
        return submitResponseDate;
    }

    public void setSubmitResponseDate(Date submitResponseDate) {
        this.submitResponseDate = submitResponseDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getInputSubCode() {
        return inputSubCode;
    }

    public void setInputSubCode(String inputSubCode) {
        this.inputSubCode = inputSubCode;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getChannelMsgId() {
        return channelMsgId;
    }

    public void setChannelMsgId(String channelMsgId) {
        this.channelMsgId = channelMsgId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(boolean submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }
}
