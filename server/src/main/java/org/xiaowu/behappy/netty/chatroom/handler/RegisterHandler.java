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
import org.xiaowu.behappy.netty.chatroom.service.RegisterService;

/**
 * 监听登录数据
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterHandler {

    private final RegisterService registerService;

    @OnEvent(EventNam.REGISTER)
    public void onData(SocketIOClient client, User data, AckRequest ackSender) throws Exception {
        log.debug("用户注册: {}", data.getName());
        registerService.register(data, client);
    }
}
