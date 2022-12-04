package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.constant.Common;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.StatusType;
import org.xiaowu.behappy.netty.chatroom.constant.SystemType;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import java.util.Objects;

/**
 *
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final SocketIOServer socketIOServer;

    private final StoreService storeService;

    @OnEvent(EventNam.LOGOUT)
    public void onData(SocketIOClient client, AckRequest ackSender) throws Exception {
        User user = (User) client.get(Common.USER_KEY);
        log.debug("用户退出: {}", user.getName());
        // 判断是否是已登录用户
        if (Objects.nonNull(user)) {
            client.del(Common.USER_KEY);
            if (StrUtil.isNotBlank(user.getId())) {
                // 修改登录用户信息并通知所有在线用户
                socketIOServer.getBroadcastOperations().sendEvent(EventNam.SYSTEM, user, SystemType.LOGOUT.getName());
                storeService.saveOrUpdateUser(user, StatusType.LOGOUT);
            }
        }
    }
}
