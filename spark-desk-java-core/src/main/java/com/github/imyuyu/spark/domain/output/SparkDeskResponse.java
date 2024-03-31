package com.github.imyuyu.spark.domain.output;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.NoArgsConstructor
@lombok.Data
public class SparkDeskResponse {
    @JsonProperty("header")
    private RespHeader header;
    @JsonProperty("payload")
    private RespPayload payload;
}
