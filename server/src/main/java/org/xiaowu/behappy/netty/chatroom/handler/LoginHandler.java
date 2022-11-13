package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.netty.channel.ChannelHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;

/**
 * 监听登录数据
 * @author xiaowu
 */
@Slf4j
@RequiredArgsConstructor
public class LoginHandler implements DataListener<User> {

    private final LoginService loginService;

    @Override
    public void onData(SocketIOClient client, User data, AckRequest ackSender) throws Exception {
        log.debug("用户登录: {}", data.getName());
        loginService.login(data, client, false);
    }
}
