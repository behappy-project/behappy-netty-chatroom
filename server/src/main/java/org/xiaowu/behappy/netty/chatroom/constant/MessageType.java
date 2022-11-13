package org.xiaowu.behappy.netty.chatroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发送消息类型
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT("text"),IMAGE("image");

    private final String name;
}
