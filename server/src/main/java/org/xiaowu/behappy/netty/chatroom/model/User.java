package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author xiaowu
 */
@Data
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2594108297042636782L;
    private String id;

    private String name;

    private String password;

    /**
     * 登录时间戳
     */
    private Long time;

    private String avatarUrl;

    private String ip;

    private String deviceType;

    private String roomId;

    private String type;

}
