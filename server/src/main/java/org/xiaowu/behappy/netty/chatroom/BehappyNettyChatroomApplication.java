package org.xiaowu.behappy.netty.chatroom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BehappyNettyChatroomApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BehappyNettyChatroomApplication.class, args);
    }

    /**
     * 主线程阻塞
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Thread.currentThread().join();
    }
}
