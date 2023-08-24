package com.drondea.simplesms.sender;

import com.drondea.simplesms.bean.Channel;
import com.drondea.simplesms.config.ChannelConfigure;
import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.sms.session.AbstractClientSessionManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author volcano
 * @version V1.0
 * @date 2019年9月15日下午11:53:51
 */
@Component
public class SenderStarter {

    /**
     * 通道对应的SessionManager
     */
    private final static Map<String, AbstractClientSessionManager> CHANNEL_SESSION_MANAGER = new ConcurrentHashMap<>();
    private final static Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    @Resource
    private ChannelConfigure channelConfigure;

    @Resource(name = "cmppEnv")
    private CmppEnv cmppEnv;

    /**
     * 启动通道
     */
    @PostConstruct
    public void start() {
        Channel chinaMobile = channelConfigure.getChinaMobile();
        chinaMobile.setOperator("chinaMobile");
        startChannel(chinaMobile);
        CHANNEL_MAP.put(chinaMobile.getChannelNo(), chinaMobile);
    }

    /**
     * 启动通道
     * @param channel
     */
    public void startChannel(Channel channel) {
        //创建连接
        createSessionManager(channel);
    }

    private void createSessionManager(Channel channel) {
        if (ProtocolType.CMPP.equals(channel.getProtocolType())) {
            cmppEnv.createClient(channel);
        }
    }

    public static AbstractClientSessionManager getClientManager(String key) {
        return CHANNEL_SESSION_MANAGER.get(key);
    }

    /**
     * 获取活跃的通道信息
     * @return
     */
    public static Map<String, AbstractClientSessionManager> getChannelSessionManager() {
        return CHANNEL_SESSION_MANAGER;
    }

    public static Channel getChannelByNo(String no) {
        return CHANNEL_MAP.get(no);
    }
}
