package org.xiaowu.behappy.netty.chatroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 发送消息类型
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT("text"),IMAGE("image");

    private final String name;

    public static MessageType getTypeByName(String name){
        return Arrays.stream(MessageType.values())
                .filter(original -> original.getName().equals(name))
                .findFirst()
                .orElse(MessageType.TEXT);
    }
}
