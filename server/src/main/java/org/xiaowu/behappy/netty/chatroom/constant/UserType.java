package org.xiaowu.behappy.netty.chatroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum UserType {
    USER("user"),GROUP("group");

    private final String name;
}
