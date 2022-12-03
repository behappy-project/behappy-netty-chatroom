package org.xiaowu.behappy.netty.chatroom;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.xiaowu.behappy.netty.chatroom.model.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class BehappyNettyChatroomApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedis() {
        //User user = new User();
        //user.setId("test1");
        //user.setName("test1");
        //Map<String, Object> map = CBeanUtils.beanToMapNotIgnoreNullValue(user);
        //User user2 = new User();
        //user2.setId("test2");
        //user2.setName("test2");
        //Map<String, Object> map2 = CBeanUtils.beanToMapNotIgnoreNullValue(user2);
        //redisTemplate.opsForHash().putAll(user.getId(), map);
        //redisTemplate.opsForHash().putAll(user2.getId(), map2);

        Map<Object, Object> entries = redisTemplate.opsForHash().entries("test1");
        User user = BeanUtil.mapToBean(entries, User.class, true, CopyOptions.create());
        System.out.println(user);
    }

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
        map.put("name", "123");

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
