package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.netty.chatroom.constant.Common;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.MessageType;
import org.xiaowu.behappy.netty.chatroom.constant.UserType;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

/**
 * 监听接收消息
 * @author xiaowu
 */
@Slf4j
@RequiredArgsConstructor
public class MessageHandler implements DataListener<Message> {

    private final StoreService storeService;

    @Override
    public void onData(SocketIOClient client, Message data, AckRequest ackSender) throws Exception {
        // 判断是指定发送方发送消息,还是群发
        User user = (User) client.get(Common.USER_KEY);
        if (UserType.USER.getName().equals(data.getTo().getType())) {
            // 向所属room发消息
            SocketIO.server.getRoomOperations(data.getTo().getRoomId())
                    .sendEvent(EventNam.MESSAGE,
                            user,
                            data.getTo(),
                            data.getContent(),
                            data.getType());
        }
        if (UserType.GROUP.getName().equals(data.getTo().getType())) {
            // 群发
            SocketIO.server.getBroadcastOperations().sendEvent(EventNam.MESSAGE,
                    user,
                    data.getTo(),
                    data.getContent(),
                    data.getType());
            storeService.saveMessage(data.getFrom(), data.getTo(), data.getContent(), MessageType.valueOf(data.getType()));
        }
    }
}