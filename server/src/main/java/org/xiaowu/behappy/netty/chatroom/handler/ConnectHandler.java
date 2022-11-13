package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.*;

/**
 * 监听连接事件
 * @author xiaowu
 */
@RequiredArgsConstructor
public class ConnectHandler implements ConnectListener {

    private final AppConfiguration appConfiguration;

    private final LoginService loginService;

    @Override
    public void onConnect(SocketIOClient client) {
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
