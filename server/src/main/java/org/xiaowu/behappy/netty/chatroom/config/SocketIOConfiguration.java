package org.xiaowu.behappy.netty.chatroom.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * SocketIo初始化
 * @author xiaowu
 */
@Component
@RequiredArgsConstructor
public class SocketIOConfiguration {

    private final AppConfiguration appConfiguration;

    private final RedissonClient redissonClient;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(appConfiguration.getHost());
        config.setPort(appConfiguration.getPort());
        // 设置允许最大帧长度
        config.setMaxFramePayloadLength(1024 * 1024);
        // 允许最大http content length
        config.setMaxHttpContentLength(1024 * 1024);
        config.setAllowCustomRequests(true);
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        SocketConfig sockConfig = new SocketConfig();
        // 服务端ChannelOption.SO_REUSEADDR, 地址重用, 应对address is in use
        sockConfig.setReuseAddress(true);

        config.setSocketConfig(sockConfig);
        config.setStoreFactory(new RedissonStoreFactory(redissonClient));
        return new SocketIOServer(config);
    }

    /**
     * 开启 SocketIOServer 注解支持
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

}
