package com.github.imyuyu.spark.listener;

import com.github.imyuyu.spark.domain.StopWatch;
import com.github.imyuyu.spark.domain.input.SparkDeskRequest;
import com.github.imyuyu.spark.domain.output.RespPayload;

/**
 * 空监听，啥也不做
 */
public class EmptyChatListener implements ChatListenerStoppable {

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

    }
}
