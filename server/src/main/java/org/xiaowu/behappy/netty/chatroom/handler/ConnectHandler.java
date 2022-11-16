package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.listener.ConnectListener;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.constant.EventNam;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.TOKEN;

/**
 * 监听连接事件
 * @author xiaowu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectHandler {

    private final AppConfiguration appConfiguration;

    private final LoginService loginService;


    @OnConnect
    public void onConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        log.info("客户端：{} 已连接，urlParams:{}", client.getSessionId(), urlParams);
        HttpHeaders httpHeaders = client.getHandshakeData().getHttpHeaders();
        String token = httpHeaders.get(TOKEN);
        User user = null;
        if (StrUtil.isNotBlank(token) && JWTUtil.verify(token, appConfiguration.getTokenKey().getBytes(StandardCharsets.UTF_8))) {
            JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
            user = JSONUtil.toBean(payloads, User.class);
        }

        // 判断用户是否已经登录, 已登录的用户需要重新登录
        if (Objects.nonNull(user) && StrUtil.isNotBlank(user.getId())) {
            loginService.login(user, client, true);
        }
    }
}
