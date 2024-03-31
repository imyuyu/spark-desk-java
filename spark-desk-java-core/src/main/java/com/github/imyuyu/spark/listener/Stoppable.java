package com.github.imyuyu.spark.listener;

/**
 * 可中断响应接口
 */
public interface Stoppable {
    /**
     * 是否应该中断响应
     * @return 返回true，则连接会被停止
     */
    default boolean stop() {
        return false;
    }
}
