package com.drondea.simplesms.bean;

/**
 * @author liuyanning
 */
public class User {
    private String userName;
    private String password;
    private String spNumber;
    private Integer channelLimit;
    private Integer qpsLimit;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(String spNumber) {
        this.spNumber = spNumber;
    }

    public Integer getChannelLimit() {
        return channelLimit;
    }

    public void setChannelLimit(Integer channelLimit) {
        this.channelLimit = channelLimit;
    }

    public Integer getQpsLimit() {
        return qpsLimit;
    }

    public void setQpsLimit(Integer qpsLimit) {
        this.qpsLimit = qpsLimit;
    }
}
