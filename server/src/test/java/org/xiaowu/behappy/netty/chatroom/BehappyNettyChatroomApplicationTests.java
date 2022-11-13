package org.xiaowu.behappy.netty.chatroom;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.util.CBeanUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class BehappyNettyChatroomApplicationTests {

    @Test
    void contextLoads() {
        DateTime now = DateTime.now();
        DateTime dateTime = now.offsetNew(DateField.DAY_OF_YEAR, 1);
        Map<String, Object> map = new HashMap<>();
        //签发时间
        map.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        map.put(JWTPayload.EXPIRES_AT, dateTime);
        //生效时间
        map.put(JWTPayload.NOT_BEFORE, now);
        map.put("name","123");

        String token = JWTUtil.createToken(map, "123".getBytes(StandardCharsets.UTF_8));
        System.out.println(token);

        boolean verify = JWTUtil.verify(token, "123".getBytes(StandardCharsets.UTF_8));
        System.out.println(verify);

        JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
        System.out.println(payloads);

        User user = JSONUtil.toBean(payloads, User.class);
        System.out.println(user);

    }

}
