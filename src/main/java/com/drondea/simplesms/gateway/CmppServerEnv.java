package com.drondea.simplesms.gateway;

import com.drondea.simplesms.enums.ProtocolType;
import com.drondea.simplesms.gateway.handler.CmppServerCustomHandler;
import com.drondea.simplesms.gateway.message.TcpMessageProvider;
import com.drondea.simplesms.gateway.service.TcpValidator;
import com.drondea.sms.conf.cmpp.CmppServerSocketConfig;
import com.drondea.sms.session.cmpp.CmppServerSessionManager;
import com.drondea.sms.type.CmppConstants;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * cmpp网关
 *
 * @author zly
 */
@Component
public class CmppServerEnv {
    public static final String CMPP_SERVER_ID = "cmpp_server";

    private static final Logger logger = LoggerFactory.getLogger(CmppServerEnv.class);

    public static CmppServerSessionManager start() {
        ResourceLeakDetector.setLevel(Level.ADVANCED);
        CmppServerSocketConfig socketConfig = new CmppServerSocketConfig(CMPP_SERVER_ID, 7891);
        //服务器端默认版本号2.0
        socketConfig.setVersion(CmppConstants.VERSION_20);
        CmppServerCustomHandler customHandler = new CmppServerCustomHandler();
        CmppServerSessionManager sessionManager = new CmppServerSessionManager(new TcpValidator(ProtocolType.CMPP), socketConfig, customHandler);
        sessionManager.setMessageProvider(new TcpMessageProvider());
        sessionManager.doOpen();
        logger.info("启动CMPP网关，端口:" + 7891);
        return sessionManager;
    }
}