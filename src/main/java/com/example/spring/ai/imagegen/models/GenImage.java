package com.example.spring.ai.imagegen.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collation = "generated_images")
public class GenImage {
    @Id
    private String imageId;
    private String path;
    private String userId;
    private String prompt;
    @CreatedDate
    private Date creationDate;
}
