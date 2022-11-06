package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.netty.channel.ChannelHandler;
import org.xiaowu.behappy.netty.chatroom.model.User;

/**
 * 监听登录数据
 * @author xiaowu
 */
public class LoginHandler implements DataListener<User> {
    @Override
    public void onData(SocketIOClient client, User data, AckRequest ackSender) throws Exception {

    }
}
