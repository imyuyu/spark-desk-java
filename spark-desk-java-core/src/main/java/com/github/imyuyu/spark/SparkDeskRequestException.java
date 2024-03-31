package com.github.imyuyu.spark;

import com.github.imyuyu.spark.domain.output.RespHeader;

/**
 * 请求错误
 */
public class SparkDeskRequestException extends RuntimeException {
    private int code;

    public SparkDeskRequestException(int code) {
        this.code = code;
    }

    public SparkDeskRequestException(String message, int code) {
        super(message);
        this.code = code;
    }

    public SparkDeskRequestException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public SparkDeskRequestException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public SparkDeskRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
