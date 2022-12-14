package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
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
        HttpHeaders httpHeaders = client.getHandshakeData().getHttpHeaders();
        String token = httpHeaders.get(TOKEN);
        log.info("客户端：{} 已连接, token: {}, urlParams:{}", client.getSessionId(), token, urlParams);
        User user = null;
        if (StrUtil.isNotBlank(token)) {
            try {
                if (JWTUtil.verify(token, appConfiguration.getTokenKey().getBytes(StandardCharsets.UTF_8))){
                    JWTValidator.of(token).validateDate();
                    JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
                    user = JSONUtil.toBean(payloads, User.class);
                }
            } catch (ValidateException e) {
                // token失效, 需要用户回到登录页面重新登录
                log.error("token失效, 需要用户回到登录页面重新登录...");
                client.sendEvent(EventNam.SERVER_ERR,"请重新登录");
                return;
            }
        }

        // 判断用户是否已经登录, 已登录的用户刷新token
        if (Objects.nonNull(user) && StrUtil.isNotBlank(user.getId())) {
            loginService.login(user, client, true);
        }
    }
}
