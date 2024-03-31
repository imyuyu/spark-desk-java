package com.github.imyuyu.spark.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.imyuyu.spark.enums.SparkVersion;

import java.io.IOException;

/**
 * 序列化
 */
public class SparkVersionEnumSerializer extends JsonSerializer<SparkVersion> {
    @Override
    public void serialize(SparkVersion sparkVersion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(sparkVersion.getDomain());
    }
}
