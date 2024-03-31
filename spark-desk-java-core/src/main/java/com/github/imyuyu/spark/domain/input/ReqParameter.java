package com.github.imyuyu.spark.domain.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.imyuyu.spark.enums.SparkVersion;
import com.github.imyuyu.spark.utils.json.SparkVersionEnumSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * parameter part
 */
@Data
@Accessors(chain = true)
public class ReqParameter {
    private Chat chat;

    /**
     * chat part
     */
    @lombok.NoArgsConstructor
    @lombok.Data
    @Accessors(chain = true)
    public static class Chat {
        /**
         * 指定访问的领域:
         * general指向V1.5版本;
         * generalv2指向V2版本;
         * generalv3指向V3版本;
         * generalv3.5指向V3.5版本;
         * 注意：不同的取值对应的url也不一样！
         */
        @JsonProperty("domain")
        @JsonSerialize(using = SparkVersionEnumSerializer.class)
        private SparkVersion version;
        /**
         * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
         */
        private float temperature;
        /**
         * 模型回答的tokens的最大长度
         */
        @JsonProperty("max_tokens")
        private int maxTokens;
        /**
         * 从k个候选中随机选择⼀个（⾮等概率）
         */
        @JsonProperty("top_k")
        private int topK;
        /**
         * 用于关联用户会话
         */
        @JsonProperty("chat_id")
        private String chatId;
    }

}
