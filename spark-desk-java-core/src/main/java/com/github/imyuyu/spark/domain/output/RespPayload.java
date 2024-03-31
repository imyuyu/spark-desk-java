package com.github.imyuyu.spark.domain.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.imyuyu.spark.enums.ChatRole;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class RespPayload {
    @JsonProperty("choices")
    private Choices choices;
    @JsonProperty("usage")
    private Usage usage;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class Choices {
        @JsonProperty("status")
        private Integer status;
        @JsonProperty("seq")
        private Integer seq;
        @JsonProperty("text")
        private List<Text> text;

        @lombok.NoArgsConstructor
        @lombok.Data
        public static class Text {
            @JsonProperty("content")
            private String content;
            @JsonProperty("role")
            private ChatRole role;
            @JsonProperty("index")
            private Integer index;
        }
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class Usage {
        @JsonProperty("text")
        private Text text;

        @lombok.NoArgsConstructor
        @lombok.Data
        public static class Text {
            @JsonProperty("question_tokens")
            private Integer questionTokens;
            @JsonProperty("prompt_tokens")
            private Integer promptTokens;
            @JsonProperty("completion_tokens")
            private Integer completionTokens;
            @JsonProperty("total_tokens")
            private Integer totalTokens;
        }
    }
}