package com.github.imyuyu.spark;

import com.github.imyuyu.spark.domain.output.RespHeader;

/**
 * 响应错误，一般是请求成功后，消息接口返回的错误 详细释义可在接口说明文档最后的错误码说明了解
 * {@code https://www.xfyun.cn/document/error-code?code=}
 */
public class SparkDeskResponseException extends RuntimeException {
    private RespHeader header;

    public SparkDeskResponseException(RespHeader header) {
        this.header = header;
    }

    public SparkDeskResponseException(String message, RespHeader header) {
        super(message);
        this.header = header;
    }

    public SparkDeskResponseException(String message, Throwable cause, RespHeader header) {
        super(message, cause);
        this.header = header;
    }

    public SparkDeskResponseException(Throwable cause, RespHeader header) {
        super(cause);
        this.header = header;
    }

    public SparkDeskResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RespHeader header) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.header = header;
    }
}
