package com.github.imyuyu.spark.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息
 */
@Data
public class SparkMessage {
    private List<Throwable> errors = new ArrayList<>();
    private List<Object> successMsgs = new ArrayList<>();

    public void addError(Throwable e) {
        this.errors.add(e);
    }

    /**
     * 抛出异常
     */
    public void throwException() throws Throwable {
        if(errors != null && !errors.isEmpty()){
            throw errors.get(0);
        }
    }

    public void addSuccessMsg(Object content) {
        this.successMsgs.add(content);
    }

    public String getFullMessage() {
        return successMsgs.stream().map(Object::toString).collect(Collectors.joining(""));
    }
}
