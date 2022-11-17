package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;

/**
 * 监听登录数据
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHandler {

    private final LoginService loginService;

    @OnEvent(EventNam.LOGIN)
    public void onData(SocketIOClient client, User data, AckRequest ackSender) throws Exception {
        log.debug("用户登录: {}", data.getName());
        loginService.login(data, client, false);
    }
}
