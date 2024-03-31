package com.github.imyuyu.spark;

import com.github.imyuyu.spark.domain.StopWatch;
import com.github.imyuyu.spark.domain.input.SparkDeskRequest;
import com.github.imyuyu.spark.domain.output.RespPayload;
import com.github.imyuyu.spark.listener.ChatListenerStoppable;
import com.github.imyuyu.spark.spring.core.SparkTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@SpringBootApplication(scanBasePackages = "com.github.imyuyu.spark")
class TestApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Autowired
    private SparkTemplate sparkTemplate;

    @GetMapping("/chat")
    public void chat(HttpServletResponse response) throws Exception{
        response.setContentType("text/html;charset=UTF-8");
        sparkTemplate.chatStreamWithSync("hello!", new ChatListenerStoppable() {
            @Override
            public void onOpen(SparkDeskRequest sparkDeskRequest) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish(StopWatch stopWatch, RespPayload.Usage usage) {

            }

            @Override
            public void onMessage(String message) {
                try {
                    response.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
                    response.getOutputStream().flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        response.getOutputStream().write("\nover".getBytes(StandardCharsets.UTF_8));
    }
}