package com.example.spring.ai.imagegen.config;

import org.springframework.ai.image.ImageClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public ImageClient imageClient() {
        return new OpenAiImageClient(new OpenAiImageApi(apiKey));
    }
}
