package org.xiaowu.behappy.netty.chatroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum SystemType {
    JOIN("join"),LOGOUT("logout");

    private final String name;
}
