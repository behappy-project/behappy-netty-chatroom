package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.constant.Common;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.StatusType;
import org.xiaowu.behappy.netty.chatroom.constant.SystemType;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import java.util.Objects;

/**
 * 监听连接断开事件
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DisconnectHandler {

    private final StoreService storeService;

    private final SocketIOServer socketIOServer;

    /**
     * 此处经常会因`transport close`断开连接, 见
     * https://github.com/socketio/socket.io/issues/3025
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.debug("用户断开链接: {}", client.getSessionId().toString());
    }
}
