package com.drondea.simplesms.bean;

/**
 * 通道对象
 * @author liuyanning
 */
public class Channel {
    private String channelNo;
    private String protocolType;
    private String account;
    private String password;
    private String spNumber;
    private Integer channelSize;
    private Integer speed;
    private String ip;
    private Integer port;
    private String version;
    private String serviceId;
    private Short feeUserType;
    private String feeTerminalId;
    private String msgSrc;
    private String operator;

    public String getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(String spNumber) {
        this.spNumber = spNumber;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getChannelSize() {
        return channelSize;
    }

    public void setChannelSize(Integer channelSize) {
        this.channelSize = channelSize;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Short getFeeUserType() {
        return feeUserType;
    }

    public void setFeeUserType(Short feeUserType) {
        this.feeUserType = feeUserType;
    }

    public String getFeeTerminalId() {
        return feeTerminalId;
    }

    public void setFeeTerminalId(String feeTerminalId) {
        this.feeTerminalId = feeTerminalId;
    }

    public String getMsgSrc() {
        return msgSrc;
    }

    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
