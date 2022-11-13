package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

/**
 * login success dto
 * @author xiaowu
 */
@Data
public class LoginSuccessData {

    private User user;

    private String token;
}
