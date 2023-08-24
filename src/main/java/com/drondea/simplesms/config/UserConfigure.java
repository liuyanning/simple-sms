package com.drondea.simplesms.config;

import com.drondea.simplesms.bean.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author liuyanning
 */
@ConfigurationProperties(prefix = "drondea.userinfo")
public class UserConfigure {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
