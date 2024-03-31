package com.github.imyuyu.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.imyuyu.spark.domain.SparkMessage;
import com.github.imyuyu.spark.domain.StopWatch;
import com.github.imyuyu.spark.domain.input.ReqHeader;
import com.github.imyuyu.spark.domain.input.ReqParameter;
import com.github.imyuyu.spark.domain.input.ReqPayload;
import com.github.imyuyu.spark.domain.input.SparkDeskRequest;
import com.github.imyuyu.spark.domain.output.RespHeader;
import com.github.imyuyu.spark.domain.output.RespPayload;
import com.github.imyuyu.spark.domain.output.SparkDeskResponse;
import com.github.imyuyu.spark.listener.ChatListenerStoppable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 内部使用
 */
@Slf4j
class SparkWebSocketListener extends WebSocketListener {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final SparkMessage sparkMessage = new SparkMessage();
    private final Collection<ReqPayload.Message.Content> messages;
    private final ChatListenerStoppable chatListener;
    private final SparkDeskClient sparkDeskClient;
    private WebSocket webSocket;
    private String uid;
    private final StopWatch stopWatch = new StopWatch();

    public SparkWebSocketListener(Collection<ReqPayload.Message.Content> messages, ChatListenerStoppable chatListener, SparkDeskClient sparkDeskClient) {
        this(null, messages, chatListener, sparkDeskClient);
    }

    public SparkWebSocketListener(String uid, Collection<ReqPayload.Message.Content> messages, ChatListenerStoppable chatListener, SparkDeskClient sparkDeskClient) {
        this.uid = uid;
        this.messages = messages;
        this.chatListener = chatListener;
        this.sparkDeskClient = sparkDeskClient;

        if (uid == null || uid.isEmpty()) {
            this.uid = UUID.randomUUID().toString().substring(0, 10);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        try {
            if (null != response) {
                int code = response.code();
                ResponseBody body = response.body();
                String bodyStr = body != null ? body.string() : "";
                if (101 != code) {
                    log.error("connection failed");
                }
                SparkDeskRequestException sparkDeskRequestException = new SparkDeskRequestException(bodyStr, code);
                sparkMessage.addError(sparkDeskRequestException);
                chatListener.onError(sparkDeskRequestException);
            }
        } catch (IOException e) {
            log.error("", e);
            sparkMessage.addError(e);
            chatListener.onError(e);
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {

        try {
            SparkDeskResponse sparkDeskResponse = sparkDeskClient.getObjectMapper().readValue(text, SparkDeskResponse.class);
            if (chatListener.stop()) {
                //
                if (log.isDebugEnabled()) {
                    log.debug("接收响应过程中手动触发终端响应，uid: {}", uid);
                }
                // 触发结束
                stopWatch.stop(true);
                chatListener.onStop(stopWatch);
                countDownLatch.countDown();
                return;
            }

            RespHeader header = sparkDeskResponse.getHeader();
            if (header.isOk()) {
                List<RespPayload.Choices.Text> texts = sparkDeskResponse.getPayload().getChoices().getText();
                if (texts != null && !texts.isEmpty()) {
                    for (RespPayload.Choices.Text t : texts) {
                        sparkMessage.addSuccessMsg(t.getContent());
                        chatListener.onMessage(t.getContent());
                    }
                }
                if (header.isEnd()) {
                    stopWatch.stop();
                    chatListener.onFinish(stopWatch, sparkDeskResponse.getPayload().getUsage());
                    countDownLatch.countDown();
                }
            } else {
                SparkDeskResponseException sparkDeskResponseException = new SparkDeskResponseException(header);
                sparkMessage.addError(sparkDeskResponseException);
                chatListener.onError(sparkDeskResponseException);
                countDownLatch.countDown();
            }
        } catch (JsonProcessingException e) {
            log.error("", e);
            sparkMessage.addError(e);
            chatListener.onError(e);
            countDownLatch.countDown();
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        stopWatch.connected();
        this.webSocket = webSocket;
        if (chatListener.stop()) {
            //
            if (log.isDebugEnabled()) {
                log.debug("打开连接前手动触发终端响应，uid: {}", uid);
            }
            stopWatch.stop(true);
            chatListener.onStop(stopWatch);
            countDownLatch.countDown();
            return;
        }
        // 在此处推送消息
        SparkDeskRequest sparkDeskRequest = new SparkDeskRequest();
        sparkDeskRequest.setHeader(new ReqHeader().setAppId(sparkDeskClient.getAppId()).setUid(uid))
                .setParameter(new ReqParameter().setChat(new ReqParameter.Chat()
                        .setVersion(sparkDeskClient.getVersion())
                        .setTemperature(sparkDeskClient.getTemperature())
                        .setMaxTokens(sparkDeskClient.getMaxTokens())
                        .setTopK(sparkDeskClient.getTopK())
                        .setChatId(uid)
                ))
                .setPayload(new ReqPayload().setMessage(new ReqPayload.Message().setText(messages)))
        ;
        // send event
        chatListener.onOpen(sparkDeskRequest);
        try {
            stopWatch.start();
            webSocket.send(sparkDeskClient.getObjectMapper().writeValueAsString(sparkDeskRequest));
        } catch (JsonProcessingException e) {
            log.error("", e);
            sparkMessage.addError(e);
            chatListener.onError(e);
        }
    }

    /**
     * 坚持住！！！
     */
    @SneakyThrows
    public SparkMessage holdOn() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            closeWebSocket();
        }

        sparkMessage.throwException();

        return sparkMessage;
    }

    /**
     * 坚持住！！！
     *
     * @param timeout
     * @param unit
     * @return
     */
    @SneakyThrows
    public SparkMessage holdOn(long timeout, TimeUnit unit) {
        try {
            if (!countDownLatch.await(timeout, unit)) {
                if (log.isDebugEnabled()) {
                    log.debug("本次请求响应时间超过:{} {}, uid: {}", timeout, unit, uid);
                }
            }
        } catch (InterruptedException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            closeWebSocket();
        }

        sparkMessage.throwException();

        return sparkMessage;
    }

    /**
     * 关闭websocket
     */
    private void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "");
        }
    }
}
