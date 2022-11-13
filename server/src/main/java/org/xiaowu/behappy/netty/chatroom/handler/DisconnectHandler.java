package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class DisconnectHandler implements DisconnectListener {

    private final StoreService storeService;

    @Override
    public void onDisconnect(SocketIOClient client) {
        log.debug("用户断开链接: {}", client.getSessionId().toString());
        // 判断是否是已登录用户
        User user = (User) client.get(Common.USER_KEY);
        if (Objects.nonNull(user) && StrUtil.isNotBlank(user.getId())) {
            // 修改登录用户信息并通知所有在线用户
            SocketIO.server.getBroadcastOperations().sendEvent(EventNam.SYSTEM, user, SystemType.LOGOUT);
            storeService.saveUser(user, StatusType.LOGOUT);
        }
    }
}
