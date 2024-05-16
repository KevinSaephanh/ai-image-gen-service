package com.example.spring.ai.imagegen.controllers;

import com.example.spring.ai.imagegen.models.GenImage;
import com.example.spring.ai.imagegen.requests.ImageGenRequest;
import com.example.spring.ai.imagegen.services.ImageGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/ai/image")
public class ImageGenController {
    @Autowired
    private ImageGenService imageGenService;

    @PostMapping("/generate")
    public ResponseEntity<GenImage> generateImage(@RequestBody ImageGenRequest req) {
        log.info("ImageGenController generateImage: generating image with: {}", req);
        GenImage genImage = imageGenService.generateImage(req);
        return ResponseEntity.status(201).body(genImage);
    }

    @GetMapping("/user/{userId}/images")
    public ResponseEntity<List<GenImage>> findUserGenImages(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "imageId") String sort) {
        log.info("ImageGenController findUserGenImages: finding images with req: {} {} {} {}", userId, page, size, sort);
        List<GenImage> images = imageGenService.findUserGenImages(userId, page, size, sort);
        return ResponseEntity.ok(images);
    }
}
