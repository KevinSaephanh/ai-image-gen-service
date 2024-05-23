package com.example.spring.ai.imagegen.controllers;

import com.example.spring.ai.imagegen.models.GenImage;
import com.example.spring.ai.imagegen.requests.ImageGenRequest;
import com.example.spring.ai.imagegen.services.ImageGenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai/image")
public class ImageGenController {
    private static final Logger logger = LogManager.getLogger(ImageGenController.class);

    @Autowired
    private ImageGenService imageGenService;

    @PostMapping("/generate")
    public ResponseEntity<GenImage> generateImage(@RequestBody ImageGenRequest req) {
        logger.info("ImageGenController generateImage: generating image with: {}", req);
        GenImage genImage = imageGenService.generateImage(req);
        return ResponseEntity.status(201).body(genImage);
    }

    @GetMapping("/user/{userId}/images")
    public ResponseEntity<List<GenImage>> findUserGenImages(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "imageId") String sort) {
        logger.info("ImageGenController findUserGenImages: finding images with req: {} {} {} {}", userId, page, size, sort);
        List<GenImage> images = imageGenService.findUserGenImages(userId, page, size, sort);
        return ResponseEntity.ok(images);
    }
}
