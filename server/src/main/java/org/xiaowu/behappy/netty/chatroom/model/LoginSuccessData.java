package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

import java.io.Serializable;

/**
 * login success dto
 * @author xiaowu
 */
@Data
public class LoginSuccessData implements Serializable {

    private static final long serialVersionUID = -5080937508871176980L;
    private User user;

    private String token;
}
