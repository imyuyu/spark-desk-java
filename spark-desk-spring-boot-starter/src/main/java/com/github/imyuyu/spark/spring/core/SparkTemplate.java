package com.github.imyuyu.spark.spring.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imyuyu.spark.SparkDeskClient;
import com.github.imyuyu.spark.domain.input.ReqPayload;
import com.github.imyuyu.spark.enums.ChatRole;
import com.github.imyuyu.spark.listener.ChatListener;
import com.github.imyuyu.spark.listener.ChatListenerStoppable;
import com.github.imyuyu.spark.spring.props.SparkDeskProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SparkTemplate {

    private final SparkDeskClient sparkDeskClient;

    public SparkTemplate(ObjectMapper objectMapper, SparkDeskProperties sparkDeskProperties) {
        this.sparkDeskClient = SparkDeskClient.builder().secure(sparkDeskProperties.isSecure())
                .version(sparkDeskProperties.getVersion())
                .appId(sparkDeskProperties.getAppId())
                .apiKey(sparkDeskProperties.getApiKey())
                .apiSecret(sparkDeskProperties.getApiSecret())
                .temperature(sparkDeskProperties.getTemperature())
                .maxTokens(sparkDeskProperties.getMaxTokens())
                .topK(sparkDeskProperties.getTopK())
                .objectMapper(objectMapper)
                .build();
    }

    /**
     * 最简单的请求，一问一答
     *
     * @param message 用户问题
     * @return ai回答
     */
    @Nullable
    public String chat(@NonNull String message) {
        return sparkDeskClient.chat(message);
    }

    /**
     * 最简单的请求，一问一答
     *
     * @param message 用户问题
     * @return ai回答
     */
    @Nullable
    public String chat(@NonNull String system, @NonNull String message) {
        return sparkDeskClient.chat(system, message);
    }

    /**
     * 多轮对话，同步请求
     * @param messages
     * @return
     */
    public String chat(List<ReqPayload.Message.Content> messages) {
        return sparkDeskClient.chat(messages);
    }

    /**
     * 单论对话流式传输
     *
     * @param messages     对话内容
     * @param chatListener 消息监听
     */
    public void chatStreamWithSync(String messages, ChatListenerStoppable chatListener) {
        sparkDeskClient.chatStreamWithSync(messages, chatListener);
    }

    /**
     * 单论对话流式传输
     *
     * @param system       角色定义
     * @param message      用户消息
     * @param chatListener 消息监听
     */
    public void chatStreamWithSync(String system, String message, ChatListenerStoppable chatListener) {
        sparkDeskClient.chatStreamWithSync(system, message, chatListener);
    }

    /**
     * 多轮对话流式传输，异步处理, 需要用户自己实现线程保持，否则可能导致响应失败
     *
     * @param message      用户消息
     * @param chatListener 监听
     */
    public void chatStreamWithAsync(String message, ChatListenerStoppable chatListener) {
        sparkDeskClient.chatStreamWithAsync(message, chatListener);
    }

    /**
     * 多轮对话流式传输，异步处理，需要用户自己实现线程保持，否则可能导致响应失败
     *
     * @param system       角色定义
     * @param message      用户消息
     * @param chatListener 监听
     */
    public void chatStreamWithAsync(String system, String message, ChatListenerStoppable chatListener) {
        sparkDeskClient.chatStreamWithAsync(system, message, chatListener);
    }
}
