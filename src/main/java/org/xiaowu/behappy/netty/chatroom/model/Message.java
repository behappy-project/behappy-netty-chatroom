/**
  * Copyright 2022 json.cn
  */
package org.xiaowu.behappy.netty.chatroom.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Auto-generated: 2022-11-13 10:29:23
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -6127718690044821421L;
    private User from;

    private User to;

    private String content;

    private String type;

    private Long time;

}
