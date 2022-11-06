package org.xiaowu.behappy.netty.chatroom.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;

import java.nio.charset.StandardCharsets;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.*;

/**
 * 监听连接事件
 * @author xiaowu
 */
@RequiredArgsConstructor
public class ConnectHandler implements ConnectListener {

    private final AppConfiguration appConfiguration;

    @Override
    public void onConnect(SocketIOClient client) {
        HttpHeaders httpHeaders = client.getHandshakeData().getHttpHeaders();
        String token = httpHeaders.get(TOKEN);
        JSONObject user = new JSONObject();
        if (StrUtil.isNotBlank(token) && JWTUtil.verify(token,appConfiguration.getTokenKey().getBytes(StandardCharsets.UTF_8))){
            user = JWTUtil.parseToken(token).getPayloads();
        }
        // 判断用户是否已经登录
        if (StrUtil.isNotBlank(user.getStr("id"))){

        }
    }
}
