package org.xiaowu.behappy.netty.chatroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xiaowu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "chatroom")
public class AppConfiguration {

    private String host;

    private Integer port;

    private String tokenKey;

}
