package com.github.imyuyu.spark.domain.input;

import lombok.experimental.Accessors;

/**
 * SparkDesk web request
 * @author imyuyu
 */
@lombok.NoArgsConstructor
@lombok.Data
@Accessors(chain = true)
public class SparkDeskRequest {
    private ReqHeader header;
    private ReqParameter parameter;
    private ReqPayload payload;
}
