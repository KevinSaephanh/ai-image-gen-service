package com.example.spring.ai.imagegen.services;

import com.example.spring.ai.imagegen.models.GenImage;
import com.example.spring.ai.imagegen.repositories.GenImageRepository;
import com.example.spring.ai.imagegen.requests.ImageGenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class ImageGenService {
    @Autowired
    private ImageClient imageClient;

    @Autowired
    private GenImageRepository repository;

    @Value("${spring.ai.openai.user}")
    private String openAIUser;

    public GenImage generateImage(ImageGenRequest req) {
        if (!StringUtils.hasText(req.prompt()) || !StringUtils.hasText(req.userId())) {
            String message = "Prompt cannot be empty!";
            log.error("ImageGenService generateImage: {}", message);
            throw new IllegalArgumentException(message);
        }

        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .withQuality("hd")
                .withN(1)
                .withModel("dall-e-3")
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(req.prompt(), options);
        ImageResponse res = imageClient.call(imagePrompt);
        String imageUrl = res.getResult().getOutput().getUrl();
        log.info("ImageGenService generateImage: success: {}", imageUrl);
        return saveImageMetadata(imageUrl, req.prompt(), req.userId());
    }

    public GenImage saveImageMetadata(String imageUrl, String prompt, String userId) {
        log.info("ImageGenService saveImageMetadata: saving with: {} and {}", prompt, userId);
        String parsedPath = imageUrl.substring(imageUrl.lastIndexOf(openAIUser));
        GenImage genImage = GenImage.builder()
                .prompt(prompt)
                .userId(userId)
                .path(parsedPath)
                .build();
        genImage = repository.save(genImage);
        log.info("ImageGenService saveImageMetadata: success");
        return genImage;
    }

    public List<GenImage> findUserGenImages(String userId, int page, int size, String sort) {
        log.info("ImageGenService findUserGenImages: finding user gen images");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return repository.findByUserId(pageable, userId);
    }
}
