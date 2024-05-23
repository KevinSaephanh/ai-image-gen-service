package com.example.spring.ai.imagegen.services;

import com.example.spring.ai.imagegen.models.GenImage;
import com.example.spring.ai.imagegen.repositories.GenImageRepository;
import com.example.spring.ai.imagegen.requests.ImageGenRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@Service
public class ImageGenService {
    private static final Logger logger = LogManager.getLogger(ImageGenService.class);

    @Autowired
    private ImageClient imageClient;

    @Autowired
    private GenImageRepository repository;

    @Value("${spring.ai.openai.user}")
    private String openAIUser;

    public GenImage generateImage(ImageGenRequest req) {
        if (!StringUtils.hasText(req.prompt()) || !StringUtils.hasText(req.userId())) {
            String message = "Prompt cannot be empty!";
            logger.error("ImageGenService generateImage: {}", message);
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
        logger.info("ImageGenService generateImage: success: {}", imageUrl);
        return saveImageMetadata(imageUrl, req.prompt(), req.userId());
    }

    public GenImage saveImageMetadata(String imageUrl, String prompt, String userId) {
        logger.info("ImageGenService saveImageMetadata: saving with: {} and {}", prompt, userId);
        String parsedPath = imageUrl.substring(imageUrl.lastIndexOf(openAIUser));
        GenImage genImage = GenImage.builder()
                .prompt(prompt)
                .userId(userId)
                .path(parsedPath)
                .build();
        genImage = repository.save(genImage);
        logger.info("ImageGenService saveImageMetadata: success");
        return genImage;
    }

    public List<GenImage> findUserGenImages(String userId, int page, int size, String sort) {
        logger.info("ImageGenService findUserGenImages: finding user gen images");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return repository.findByUserId(pageable, userId);
    }
}
