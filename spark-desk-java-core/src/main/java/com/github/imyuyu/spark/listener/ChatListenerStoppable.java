package com.github.imyuyu.spark.listener;

import com.github.imyuyu.spark.domain.StopWatch;

/**
 * 流式传输时，可中断响应的监听
 */
public interface ChatListenerStoppable extends ChatListener,Stoppable {

    /**
     * 停止监听
     * @param stopWatch
     */
    default void onStop(StopWatch stopWatch) {
        // left blank
    }
}
