package org.xiaowu.behappy.netty.chatroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum StatusType {

    LOGIN("login"),LOGOUT("logout");

    private final String name;
}
