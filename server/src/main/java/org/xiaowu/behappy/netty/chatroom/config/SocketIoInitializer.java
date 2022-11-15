package org.xiaowu.behappy.netty.chatroom.config;

import cn.hutool.extra.spring.SpringUtil;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.handler.ConnectHandler;
import org.xiaowu.behappy.netty.chatroom.handler.DisconnectHandler;
import org.xiaowu.behappy.netty.chatroom.handler.LoginHandler;
import org.xiaowu.behappy.netty.chatroom.handler.MessageHandler;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.LOGIN;
import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.MESSAGE;

/**
 * SocketIo初始化
 * @author xiaowu
 */
@Component
@RequiredArgsConstructor
public class SocketIoInitializer {

    private final AppConfiguration appConfiguration;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(appConfiguration.getHost());
        config.setPort(appConfiguration.getPort());
        // 设置允许最大帧长度
        config.setMaxFramePayloadLength(1024 * 1024);
        // 允许最大http content length
        config.setMaxHttpContentLength(1024 * 1024);

        SocketConfig sockConfig = new SocketConfig();
        // 服务端ChannelOption.SO_REUSEADDR, 地址重用, 应对address is in use
        sockConfig.setReuseAddress(true);

        config.setSocketConfig(sockConfig);
        return new SocketIOServer(config);
    }

}
