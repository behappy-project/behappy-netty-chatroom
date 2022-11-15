package org.xiaowu.behappy.netty.chatroom.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.netty.chatroom.constant.UserType;
import org.xiaowu.behappy.netty.chatroom.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.USER_KEY;

/**
 *
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private List<User> users;

    private final SocketIOServer socketIOServer;

    @PostConstruct
    void initDefaultUser() {
        users = new ArrayList<>() {
            {
                // 放置一个默认聊天室给新登录的账户
                User defaultU = new User();
                defaultU.setId("group_001");
                defaultU.setName("群聊天室");
                defaultU.setAvatarUrl("static/img/avatar/group-icon.png");
                defaultU.setType(UserType.GROUP.getName());
                add(defaultU);
            }
        };
    }

    public List<User> getOnlineUsers() {
        // Returns the matching socket instances
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            User user = (User) socketIOClient.get(USER_KEY);
            if (Objects.nonNull(user)) {
                users.add(user);
            }
        });
        return users;
    }


    public boolean isHaveName(String name) {
        List<User> onlineUsers = getOnlineUsers();
        long count = onlineUsers.stream().filter(user -> user.getName().equals(name)).count();
        return count > 0;
    }
}
