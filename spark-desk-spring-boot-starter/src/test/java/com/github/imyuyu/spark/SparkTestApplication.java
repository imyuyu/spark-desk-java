package com.github.imyuyu.spark;

import com.github.imyuyu.spark.spring.core.SparkTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * test
 */
@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class SparkTestApplication {
    @Autowired
    private SparkTemplate sparkTemplate;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        String chat = sparkTemplate.chat("hello");
        Assertions.assertNotNull(chat);
    }

    @Test
    void textWeb() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/chat"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
