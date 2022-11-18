package org.xiaowu.behappy.netty.chatroom.constant;

/**
 * 事件名称
 * @author xiaowu
 */
public interface EventNam {

    String HISTORY_MESSAGE = "history-message";

    String REGISTER_FAIL = "registerFail";

    String REGISTER_SUCCESS = "registerSuccess";

    String LOGIN_FAIL = "loginFail";

    String LOGIN_SUCCESS = "loginSuccess";

    String MESSAGE = "message";

    String SYSTEM = "system";

    String DISCONNECT = "disconnect";

    String CONNECT = "connect";

    String LOGIN = "login";

    String REGISTER = "register";
}
