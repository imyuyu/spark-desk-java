package com.github.imyuyu.spark.spring.props;

import com.github.imyuyu.spark.enums.Protocol;
import com.github.imyuyu.spark.enums.SparkVersion;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 星火大模型配置文件
 */
@Data
@ConfigurationProperties("spark.desk")
public class SparkDeskProperties {
    /**
     * 使用版本
     */
    private SparkVersion version;
    /**
     * 应用appid，从开放平台控制台创建的应用中获取
     */
    private String appId;
    /**
     * 应用key
     */
    private String apiKey;
    /**
     * 应用密钥
     */
    private String apiSecret;
    /**
     * 是否使用安全传输协议，如果使用安全传输协议，将使用{@code wss://}
     */
    private boolean secure = true;
    /**
     * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高,取值范围 (0，1] ，默认值0.5
     */
    private float temperature = 0.5f;
    /**
     * 模型回答的tokens的最大长度,V1.5取值为[1,4096],V2.0、V3.0和V3.5取值为[1,8192]，默认为2048。
     */
    private int maxTokens = 2048;

    /**
     * 从k个候选中随机选择⼀个（⾮等概率）, 取值为[1，6],默认为4
     */
    private int topK = 4;
}
