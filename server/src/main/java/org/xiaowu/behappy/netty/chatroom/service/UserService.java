package org.xiaowu.behappy.netty.chatroom.service;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.netty.handler.codec.http.HttpHeaders;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.netty.chatroom.constant.Common;
import org.xiaowu.behappy.netty.chatroom.constant.UserType;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.util.IpUtils;

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

    private final SocketIOServer socketIOServer;

    private final StoreService storeService;

    List<User> initDefaultUser() {
        return new ArrayList<>() {
            {
                // 放置一个默认聊天室给新登录的账户
                User defaultU = new User();
                defaultU.setId(Common.GROUP_001_CHANNEL);
                defaultU.setName("群聊天室");
                defaultU.setAvatarUrl("static/img/avatar/group-icon.png");
                defaultU.setType(UserType.GROUP.getName());
                add(defaultU);
            }
        };
    }

    /**
     * 查询在线用户
     * @return
     */
    public List<User> getOnlineUsers() {
        List<User> users = initDefaultUser();
        // Returns the matching socket instances
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            User user = (User) socketIOClient.get(USER_KEY);
            if (Objects.nonNull(user)) {
                users.add(user);
            }
        });
        return users;
    }


    public User getUserByName(String name) {
        return storeService.getUserByName(name);
    }

    public void organizeUser(User user, SocketIOClient client){
        String ip = StrUtil.replace(client.getHandshakeData().getAddress().getHostString(), "::ffff:", "");
        HttpHeaders httpHeaders = client.getHandshakeData().getHttpHeaders();
        String realIp = httpHeaders.get("x-forwarded-for");
        String userAgent = httpHeaders.get("user-agent").toLowerCase();
        ip = StrUtil.isNotBlank(realIp) ? realIp : ip;
        String deviceType = IpUtils.getDeviceType(userAgent);
        user.setId(client.getSessionId().toString());
        user.setIp(ip);
        user.setDeviceType(deviceType);
        user.setRoomId(client.getSessionId().toString());
        user.setType(UserType.USER.getName());
        user.setTime(System.currentTimeMillis());
    }
}
