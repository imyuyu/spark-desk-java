package com.github.imyuyu.spark.domain.output;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.NoArgsConstructor
@lombok.Data
public class RespHeader {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("sid")
    private String sid;
    @JsonProperty("status")
    private Integer status;

    public boolean isOk(){
        return code == 0;
    }

    public boolean isOpen() {
        return status == 0;
    }

    public boolean isProcessing() {
        return status == 1;
    }

    public boolean isEnd() {
        return status == 2;
    }
}