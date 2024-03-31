package com.github.imyuyu.spark.domain.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.Accessors;

/**
 * header
 */
@lombok.NoArgsConstructor
@lombok.Data
@Accessors(chain = true)
public class ReqHeader {
    /**
     * 应用appid，从开放平台控制台创建的应用中获取
     */
    @JsonProperty("app_id")
    private String appId;
    /**
     * 每个用户的id，用于区分不同用户
     */
    private String uid;
}
