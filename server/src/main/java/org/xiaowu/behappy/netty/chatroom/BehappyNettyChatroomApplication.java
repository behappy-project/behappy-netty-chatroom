package org.xiaowu.behappy.netty.chatroom;

import cn.hutool.extra.spring.SpringUtil;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.handler.ConnectHandler;
import org.xiaowu.behappy.netty.chatroom.handler.DisconnectHandler;
import org.xiaowu.behappy.netty.chatroom.handler.LoginHandler;
import org.xiaowu.behappy.netty.chatroom.handler.MessageHandler;
import org.xiaowu.behappy.netty.chatroom.model.Message;
import org.xiaowu.behappy.netty.chatroom.model.User;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.LOGIN;
import static org.xiaowu.behappy.netty.chatroom.constant.EventNam.MESSAGE;

/**
 * @author 94391
 */
@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class BehappyNettyChatroomApplication implements CommandLineRunner {

    private final LoginService loginService;

    private final StoreService storeService;

    private final AppConfiguration appConfiguration;

    private final SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(BehappyNettyChatroomApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.addEventListener(LOGIN, User.class, new LoginHandler(loginService));
        socketIOServer.addEventListener(MESSAGE, Message.class, new MessageHandler(storeService, socketIOServer));
        socketIOServer.addConnectListener(new ConnectHandler(appConfiguration, loginService));
        socketIOServer.addDisconnectListener(new DisconnectHandler(storeService, socketIOServer));
        socketIOServer.startAsync().addListener(future -> {
            log.debug("server port start on {}", appConfiguration.getPort());
        });
        // 当前线程阻塞
        Thread.currentThread().join();
        socketIOServer.stop();
    }
}
