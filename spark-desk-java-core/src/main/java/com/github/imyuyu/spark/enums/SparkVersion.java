package com.github.imyuyu.spark.enums;

/**
 * SparkDesk version
 */
@lombok.AllArgsConstructor
@lombok.Getter
public enum SparkVersion {
    /**
     * 星火V3.5 现已支持system、Function Call 功能
     */
    VERSION_3_5("generalv3.5", "spark-api.xf-yun.com/v3.5/chat"),
    /**
     * 星火V2.0、V3.0和V3.5支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]六个内置插件
     */
    VERSION_3_1("generalv3", "spark-api.xf-yun.com/v3.1/chat"),
    /**
     * 星火V2.0
     */
    VERSION_2_1("generalv2", "spark-api.xf-yun.com/v2.1/chat"),
    /**
     * 星火V1.5支持[搜索]内置插件
     */
    VERSION_1_5("general", "spark-api.xf-yun.com/v1.1/chat")
    ;

    private final String domain;
    private final String requestUrl;
}
