package com.github.imyuyu.spark.listener;

import com.github.imyuyu.spark.domain.StopWatch;
import com.github.imyuyu.spark.domain.input.SparkDeskRequest;
import com.github.imyuyu.spark.domain.output.RespPayload;

/**
 * 消息监听
 */
public interface ChatListener {
    /**
     * 连接成功，支持在这个函数中定制化请求参数
     */
    default void onOpen(SparkDeskRequest sparkDeskRequest) {
        // left blank
    }

    /**
     * 异常回调
     */
    void onError(Throwable throwable);

    /**
     * 会话结束
     */
    void onFinish(StopWatch stopWatch, RespPayload.Usage usage);

    /**
     * 流式传输单次消息返回事件
     * @param message 消息内容
     */
    void onMessage(String message);
}
