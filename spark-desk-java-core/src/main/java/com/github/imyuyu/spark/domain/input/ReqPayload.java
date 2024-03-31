package com.github.imyuyu.spark.domain.input;

import com.github.imyuyu.spark.enums.ChatRole;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * payload part
 * @author imyuyu
 */
@lombok.NoArgsConstructor
@lombok.Data
@Accessors(chain = true)
public class ReqPayload {

    private Message message;

    /**
     * message part
     */
    @lombok.NoArgsConstructor
    @lombok.Data
    @Accessors(chain = true)
    public static class Message {
        private Collection<Content> text = new ArrayList<>();

        public Message addText(ChatRole role, String content) {
            text.add(new Content().setRole(role).setContent(content));
            return this;
        }

        @lombok.NoArgsConstructor
        @lombok.Data
        @Accessors(chain = true)
        public static class Content {
            /**
             * 取值为[system,user,assistant]
             * system用于设置对话背景，user表示是用户的问题，assistant表示AI的回复\
             */
            private ChatRole role;
            /**
             * 用户和AI的对话内容,所有content的累计tokens需控制8192以内
             */
            private String content;

        }
    }

}
