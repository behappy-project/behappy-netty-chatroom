package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.model.User;

import java.util.UUID;

import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.*;

/**
 *
 * @author xiaowu
 */
@Component
@RequiredArgsConstructor
public class SocketIO implements ApplicationListener<ContextRefreshedEvent> {

    private final AppConfiguration appConfiguration;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            initSocket();
        }
    }

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
        final SocketIOServer server = new SocketIOServer(config);

        server.addEventListener(LOGIN, User.class, new LoginHandler());
        server.addConnectListener(new ConnectHandler(appConfiguration));
        server.addDisconnectListener(new DisconnectHandler());
        server.start();
    }
}
