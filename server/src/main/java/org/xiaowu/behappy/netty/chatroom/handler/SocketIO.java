package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import java.util.Collection;
import java.util.UUID;

import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.*;

/**
 *
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIO implements ApplicationListener<ContextRefreshedEvent> {

    public static SocketIOServer server;

    private final AppConfiguration appConfiguration;

    private final LoginService loginService;

    private final StoreService storeService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initSocket();
    }

    @SneakyThrows
    private void initSocket() {
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
        server = new SocketIOServer(config);

        server.addEventListener(LOGIN, User.class, new LoginHandler(loginService));
        server.addEventListener(MESSAGE, Message.class, new MessageHandler(storeService));
        server.addConnectListener(new ConnectHandler(appConfiguration,loginService));
        server.addDisconnectListener(new DisconnectHandler(storeService));
        server.startAsync().addListener(future -> {
            log.debug("server port start on {}",appConfiguration.getPort());
        });
        // 当前线程阻塞
        Thread.currentThread().join();
        server.stop();
    }
}
