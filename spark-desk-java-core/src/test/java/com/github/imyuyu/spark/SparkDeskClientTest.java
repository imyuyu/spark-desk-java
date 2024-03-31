package com.github.imyuyu.spark;

import com.github.imyuyu.spark.domain.StopWatch;
import com.github.imyuyu.spark.domain.input.SparkDeskRequest;
import com.github.imyuyu.spark.domain.output.RespPayload;
import com.github.imyuyu.spark.enums.SparkVersion;
import com.github.imyuyu.spark.listener.ChatListenerStoppable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class SparkDeskClientTest {
    SparkDeskClient client;


    @BeforeEach
    void setUp() {
        client = SparkDeskClient.builder().version(SparkVersion.VERSION_3_5).appId(System.getenv("spark-desk.appid")).apiKey(System.getenv("spark-desk.appkey"))
                .apiSecret(System.getenv("spark-desk.apisecret")).build();
    }

    @Test
    void chat() {
        String chat = client.chat("你好");
        System.out.println(chat);
    }

    @Test
    void chat2() {
        String chat = client.chat("你的名字是张三", "你叫什么名字，只需要返回名字即可");
        System.out.println(chat);
        Assertions.assertEquals("张三", chat);
    }

    @Test
    void testChatStreamWithSync() {
        client.chatStreamWithSync("hello", new ChatListenerStoppable() {

            int i = 0;

            @Override
            public boolean stop() {
                return i>2;
            }

            @Override
            public void onStop(StopWatch stopWatch) {
                System.out.println("哦豁，我被禁止响应了");
            }

            @Override
            public void onOpen(SparkDeskRequest sparkDeskRequest) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish(StopWatch stopWatch, RespPayload.Usage usage) {

            }

            @Override
            public void onMessage(String message) {
                i++;
                System.out.println(message);
            }
        });
        System.out.println("request finish...");
    }


    @Test
    void testChatStreamWithAsync() throws InterruptedException {
        AtomicBoolean wsCloseFlag = new AtomicBoolean();
        client.chatStreamWithAsync("hello", new ChatListenerStoppable() {
            @Override
            public void onOpen(SparkDeskRequest sparkDeskRequest) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish(StopWatch stopWatch, RespPayload.Usage usage) {
                wsCloseFlag.set(true);
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
        System.out.println("request finish...");
        // 等待服务端返回完毕后关闭
        do {
            // System.err.println(wsCloseFlag + "---");
            Thread.sleep(200);
        } while (!wsCloseFlag.get());
    }
}