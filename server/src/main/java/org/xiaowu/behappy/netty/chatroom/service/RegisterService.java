package org.xiaowu.behappy.netty.chatroom.service;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.StatusType;
import org.xiaowu.behappy.netty.chatroom.model.User;

import java.util.Objects;

/**
 *
 * @author xiaowu
 */
@Slf4j
@Service
@AllArgsConstructor
public class RegisterService {

    private final UserService userService;

    private final StoreService storeService;

    public void register(User user, SocketIOClient client) {
        // 组织user properties
        userService.organizeUser(user,client);
        // 判断用户是否已经存在
        User dbUser = userService.getUserByName(user.getName());
        if (Objects.isNull(dbUser)) {
            // saveOrUpdate user
            storeService.saveOrUpdateUser(user, StatusType.REGISTER);
            client.sendEvent(EventNam.REGISTER_SUCCESS,"注册成功,请登录!");
        } else {
            log.warn("注册失败,昵称'{}'已存在", user.getName());
            client.sendEvent(EventNam.REGISTER_FAIL,"注册失败,昵称已存在!");
        }
    }
}
