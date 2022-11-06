package org.xiaowu.behappy.netty.chatroom.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * 监听连接断开事件
 * @author xiaowu
 */
public class DisconnectHandler implements DisconnectListener {
    @Override
    public void onDisconnect(SocketIOClient client) {
        // 判断是否是已登录用户, 如果是则删除登录用户信息并通知所有在线用户
        System.out.println("断开连接: " + client.getSessionId());
    }
}
