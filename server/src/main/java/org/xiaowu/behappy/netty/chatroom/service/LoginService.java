package org.xiaowu.behappy.netty.chatroom.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.corundumstudio.socketio.SocketIOClient;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.constant.StatusType;
import org.xiaowu.behappy.netty.chatroom.constant.SystemType;
import org.xiaowu.behappy.netty.chatroom.constant.UserType;
import org.xiaowu.behappy.netty.chatroom.handler.SocketIO;
import org.xiaowu.behappy.netty.chatroom.model.LoginSuccessData;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.util.CBeanUtils;
import org.xiaowu.behappy.netty.chatroom.util.IpUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.USER_KEY;

/**
 * 登录操作
 * @author xiaowu
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final AppConfiguration appConfiguration;

    private final UserService userService;

    private final StoreService storeService;

    public void login(User user, SocketIOClient client, boolean isReconnect) {
        String ip = StrUtil.replace(client.getHandshakeData().getAddress().getHostString(), "::ffff:", "");
        HttpHeaders httpHeaders = client.getHandshakeData().getHttpHeaders();
        String realIp = httpHeaders.get("x-forwarded-for");
        String userAgent = httpHeaders.get("user-agent").toLowerCase();
        ip = StrUtil.isNotBlank(realIp) ? realIp : ip;
        String deviceType = IpUtils.getDeviceType(userAgent);
        user.setIp(ip);
        user.setDeviceType(deviceType);
        user.setRoomId(client.getSessionId().toString());
        user.setType(UserType.USER.getName());
        // 是否需要重新登录
        if (isReconnect) {
            loginSuccess(user, client);
        } else {
            // 判断用户是否已经存在
            boolean flag = userService.isHaveName(user.getName());
            if (!flag) {
                user.setId(client.getSessionId().toString());
                user.setTime(System.currentTimeMillis());
                loginSuccess(user, client);
                // 存储user信息
                storeService.saveUser(user, StatusType.LOGIN);
                List<Message> messages = storeService.getMessages();
                client.sendEvent(EventNam.HISTORY_MESSAGE,"group_001",messages);
            } else {
                log.warn("登录失败,昵称'{}'已存在", user.getName());
                client.sendEvent(EventNam.LOGIN_FAIL,"登录失败,昵称已存在!");
            }
        }
    }

    private void loginSuccess(User user, SocketIOClient client) {
        LoginSuccessData data = new LoginSuccessData();
        data.setUser(user);

        DateTime now = DateTime.now();
        DateTime dateTime = now.offsetNew(DateField.DAY_OF_YEAR, 1);
        Map<String, Object> map = CBeanUtils.beanToMapNotIgnoreNullValue(user);
        //签发时间
        map.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        map.put(JWTPayload.EXPIRES_AT, dateTime);
        //生效时间
        map.put(JWTPayload.NOT_BEFORE, now);

        data.setToken(JWTUtil.createToken(map, appConfiguration.getTokenKey().getBytes(StandardCharsets.UTF_8)));

        // 通知有用户加入
        SocketIO.server.getBroadcastOperations().sendEvent(EventNam.SYSTEM, user, SystemType.JOIN);

        List<User> onlineUsers = userService.getOnlineUsers();
        // 为当前client赋值user
        client.set(USER_KEY, user);
        // 发送login_success事件
        client.sendEvent(EventNam.LOGIN_SUCCESS, data, onlineUsers);
    }
}
