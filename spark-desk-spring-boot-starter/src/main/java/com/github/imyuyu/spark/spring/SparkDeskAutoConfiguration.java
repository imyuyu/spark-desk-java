package com.github.imyuyu.spark.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imyuyu.spark.spring.core.SparkTemplate;
import com.github.imyuyu.spark.spring.props.SparkDeskProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(SparkDeskProperties.class)
@AutoConfiguration(
        after = JacksonAutoConfiguration.class
)
@Configuration(proxyBeanMethods = false)
public class SparkDeskAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public SparkTemplate sparkTemplate(ObjectMapper objectMapper, SparkDeskProperties sparkDeskProperties){
        SparkTemplate sparkTemplate = new SparkTemplate(objectMapper, sparkDeskProperties);

        return sparkTemplate;
    }

    @ConditionalOnMissingBean
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
