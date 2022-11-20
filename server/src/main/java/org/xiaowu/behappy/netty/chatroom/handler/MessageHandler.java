package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.constant.Common;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.MessageType;
import org.xiaowu.behappy.netty.chatroom.constant.UserType;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import java.util.UUID;

/**
 * 监听接收消息
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final StoreService storeService;

    private final SocketIOServer socketIOServer;

    @OnEvent(EventNam.MESSAGE)
    public void onData(SocketIOClient client, User from,User to, String content, String type, AckRequest ackSender) throws Exception {
        // 判断是指定发送方发送消息,还是群发
        User user = (User) client.get(Common.USER_KEY);
        if (UserType.USER.getName().equals(to.getType())) {
            // 向所属用户发消息
            SocketIOClient receiverClient = socketIOServer.getClient(UUID.fromString(to.getRoomId()));
            if (receiverClient != null && receiverClient.isChannelOpen()) {
                receiverClient.sendEvent(EventNam.MESSAGE,
                        user,
                        to,
                        content,
                        type);
            }
        }
        if (UserType.GROUP.getName().equals(to.getType())) {
            // 群发
            socketIOServer.getBroadcastOperations().sendEvent(EventNam.MESSAGE,
                    /*排除自己*/
                    client,
                    user,
                    to,
                    content,
                    type);
            storeService.saveGroupMessage(from, to, content, MessageType.getTypeByName(type));
        }
    }
}
