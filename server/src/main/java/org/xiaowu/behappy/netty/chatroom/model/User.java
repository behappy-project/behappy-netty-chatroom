package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

/**
 *
 * @author xiaowu
 */
@Data
public class User {

    private String id;

    private String name;

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
