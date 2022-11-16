package org.xiaowu.behappy.netty.chatroom;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xiaowu.behappy.netty.chatroom.config.AppConfiguration;
import org.xiaowu.behappy.netty.chatroom.service.LoginService;
import org.xiaowu.behappy.netty.chatroom.service.StoreService;

/**
 * @author 94391
 */
@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class BehappyNettyChatroomApplication implements CommandLineRunner {

    private final AppConfiguration appConfiguration;

    private final SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(BehappyNettyChatroomApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.startAsync().addListener(future -> {
            log.debug("server port start on {}", appConfiguration.getPort());
        });
    }
}
