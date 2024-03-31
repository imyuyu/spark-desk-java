package com.github.imyuyu.spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imyuyu.spark.domain.SparkMessage;
import com.github.imyuyu.spark.domain.input.ReqPayload;
import com.github.imyuyu.spark.enums.ChatRole;
import com.github.imyuyu.spark.enums.SparkVersion;
import com.github.imyuyu.spark.listener.ChatListenerStoppable;
import com.github.imyuyu.spark.listener.EmptyChatListener;
import com.github.imyuyu.spark.utils.AuthUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.*;

/**
 * 星火客户端对象，重对象，最好单例
 */
@Slf4j
@Builder
public class SparkDeskClient {

    @Getter
    private String appId;
    @Getter
    private SparkVersion version;
    @Getter
    private String apiKey;
    @Getter
    private String apiSecret;
    @Builder.Default
    @Getter
    private boolean secure = true;
    @Builder.Default
    @Getter
    private float temperature = 0.5f;
    @Builder.Default
    @Getter
    private int maxTokens = 2048;
    @Builder.Default
    @Getter
    private int topK = 4;
    @Getter
    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    public SparkDeskClient(String appId, SparkVersion version, String apiKey, String apiSecret, boolean Secure, float temperature, int maxTokens, int topK, ObjectMapper objectMapper, OkHttpClient okHttpClient) {
        this.appId = appId;
        this.version = version;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.secure = Secure;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topK = topK;
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;

        // after
        afterPropertiesSet();
    }

    private void afterPropertiesSet() {
        if (appId == null) {
            throw new NullPointerException("appId");
        }
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (apiKey == null) {
            throw new NullPointerException("version");
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().build();
        }
    }

    /**
     * 单轮对话
     * @param message
     * @return
     */
    public String chat(String message) {
        return chat(Collections.singletonList(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(message)));
    }

    /**
     * 带有消息角色定义的单论对话
     * @param system
     * @param message
     * @return
     */
    public String chat(String system, String message) {
        List<ReqPayload.Message.Content> messages = new ArrayList<>();
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.system).setContent(system));
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(message));
        return chat(messages);
    }

    /**
     * 多轮对话
     *
     * @param messages 对话内容
     * @return 本次对话ai返回结果
     */
    public String chat(List<ReqPayload.Message.Content> messages) {
        SparkWebSocketListener socketListener = new SparkWebSocketListener(messages, new EmptyChatListener(), this);
        okHttpClient.newWebSocket(createRequest(), socketListener);
        SparkMessage sparkMessage = socketListener.holdOn();
        return sparkMessage.getFullMessage();
    }

    /**
     * 单论对话流式传输
     *
     * @param messages     对话内容
     * @param chatListener 消息监听
     */
    public void chatStreamWithSync(String messages, ChatListenerStoppable chatListener) {
        chatStreamWithSync(Collections.singleton(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(messages)), chatListener);
    }

    /**
     * 单论对话流式传输
     *
     * @param system       角色定义
     * @param message      用户消息
     * @param chatListener 消息监听
     */
    public void chatStreamWithSync(String system, String message, ChatListenerStoppable chatListener) {
        List<ReqPayload.Message.Content> messages = new ArrayList<>();
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.system).setContent(system));
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(message));
        chatStreamWithSync(messages, chatListener);
    }

    /**
     * 多轮对话流式传输
     *
     * @param messages     对话内容
     * @param chatListener 消息监听
     */
    public void chatStreamWithSync(Collection<ReqPayload.Message.Content> messages, ChatListenerStoppable chatListener) {
        SparkWebSocketListener socketListener = new SparkWebSocketListener(messages, chatListener, this);
        okHttpClient.newWebSocket(createRequest(), socketListener);
        socketListener.holdOn();
    }


    public void chatStreamWithAsync(String message, ChatListenerStoppable chatListener) {
        chatStreamWithAsync(Collections.singleton(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(message)), chatListener);
    }

    /**
     * 多轮对话流式传输，异步处理
     *
     * @param system       角色定义
     * @param message      用户消息
     * @param chatListener 监听
     */
    public void chatStreamWithAsync(String system, String message, ChatListenerStoppable chatListener) {
        List<ReqPayload.Message.Content> messages = new ArrayList<>();
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.system).setContent(system));
        messages.add(new ReqPayload.Message.Content().setRole(ChatRole.user).setContent(message));
        chatStreamWithAsync(messages, chatListener);
    }

    /**
     * 多轮对话流式传输，异步处理
     *
     * @param messages     对话内容
     * @param chatListener 消息监听
     */
    public void chatStreamWithAsync(Collection<ReqPayload.Message.Content> messages, ChatListenerStoppable chatListener) {
        SparkWebSocketListener socketListener = new SparkWebSocketListener(messages, chatListener, this);
        okHttpClient.newWebSocket(createRequest(), socketListener);
    }

    private Request createRequest(){
        String authUrl = AuthUtil.getAuthUrl((secure ? "https" : "http") + "://" + version.getRequestUrl(), apiKey, apiSecret);
        return new Request.Builder().url(authUrl).build();
    }

}
