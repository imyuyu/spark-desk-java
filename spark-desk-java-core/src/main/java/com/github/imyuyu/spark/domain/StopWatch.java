package com.github.imyuyu.spark.domain;

import lombok.Getter;

/**
 * 简单的监控
 */
@Getter
public class StopWatch {
    private final long createMillis;
    /**
     * 连接时间
     */
    private long connectedMillis;
    /**
     * 开始推送消息时间
     */
    private long startMillis;
    /**
     * 消息接收完毕时间
     */
    private long endMillis;

    private boolean stop;

    public StopWatch() {
        this.createMillis = System.currentTimeMillis();
    }

    public void start(){
        this.startMillis = System.currentTimeMillis();
    }

    public void connected(){
        this.connectedMillis = System.currentTimeMillis();
    }

    public void stop(){
        this.stop(false);
    }

    /**
     *
     * @param stop 是否是手动停止
     */
    public void stop(boolean stop){
        this.endMillis = System.currentTimeMillis();
        this.stop = stop;
    }

    public long getTotalMillis(){
        return this.endMillis - this.startMillis;
    }
}
