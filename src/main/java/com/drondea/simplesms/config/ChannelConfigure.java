package com.drondea.simplesms.config;

import com.drondea.simplesms.bean.Channel;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuyanning
 */
@ConfigurationProperties(prefix = "drondea.channel")
public class ChannelConfigure {
    private Channel chinaMobile;
    private Channel chinaUnicom;
    private Channel chinaTelecom;

    public Channel getChinaMobile() {
        return chinaMobile;
    }

    public void setChinaMobile(Channel chinaMobile) {
        this.chinaMobile = chinaMobile;
    }

    public Channel getChinaUnicom() {
        return chinaUnicom;
    }

    public void setChinaUnicom(Channel chinaUnicom) {
        this.chinaUnicom = chinaUnicom;
    }

    public Channel getChinaTelecom() {
        return chinaTelecom;
    }

    public void setChinaTelecom(Channel chinaTelecom) {
        this.chinaTelecom = chinaTelecom;
    }
}
