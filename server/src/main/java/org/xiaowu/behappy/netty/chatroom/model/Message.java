/**
  * Copyright 2022 json.cn
  */
package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

/**
 * Auto-generated: 2022-11-13 10:29:23
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Message {

    private User from;

    private User to;

    private String content;

    private String type;

    private Long time;

}
