package org.xiaowu.behappy.netty.chatroom.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.constant.MessageType;
import org.xiaowu.behappy.netty.chatroom.constant.StatusType;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.util.CBeanUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.xiaowu.behappy.netty.chatroom.constant.Common.STORE_MESSAGE;

/**
 * store data
 * @author xiaowu
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String,Object> redisTemplate;

    private final AppConfiguration appConfiguration;

    @Async
    public void saveUser(User user, StatusType status) {
        log.debug("保存user: {}, StatusType: {}", user, status);
        if (status.equals(StatusType.LOGIN)) {
            Map<String, Object> map = CBeanUtils.beanToMapNotIgnoreNullValue(user);
            redisTemplate.opsForHash().putAll(user.getId(), map);
        }
    }

    @Async
    public void saveMessage(User from, User to, String message/*当是图片的时候,这里传的是base64*/, MessageType type) {
        if (MessageType.IMAGE.equals(type)) {
            String base64Data = message.replaceAll("/^data:image\\/\\w+;base64,/", "");
            byte[] dataBuffer = Base64Utils.encode(base64Data.getBytes(StandardCharsets.UTF_8));
            String filename = SecureUtil.md5(new String(dataBuffer, StandardCharsets.UTF_8));
            FileUtil.writeBytes(dataBuffer, String.format(appConfiguration.getImagePath() + File.separator + "%s.png", filename));
            message = String.format("/assets/images/%s.png", filename);
        }
        Message storeMsg = new Message();
        storeMsg.setFrom(from);
        storeMsg.setTo(to);
        storeMsg.setType(type.getName());
        storeMsg.setTime(System.currentTimeMillis());
        storeMsg.setContent(message);

        // 之后按时间排序获取
        stringRedisTemplate.opsForZSet().add(STORE_MESSAGE, JSONUtil.toJsonStr(storeMsg), storeMsg.getTime());
    }

    public List<Message> getMessages() {
        Set<String> set = stringRedisTemplate.opsForZSet().range(STORE_MESSAGE, 0, 100);

        if (CollectionUtils.isEmpty(set)) {
            return Collections.emptyList();
        }
        return set.stream().map(str -> JSONUtil.toBean(str, Message.class))
                .collect(Collectors.toList());
    }
}
