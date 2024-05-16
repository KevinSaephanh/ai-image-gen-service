package com.example.spring.ai.imagegen.repositories;

import com.example.spring.ai.imagegen.models.GenImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenImageRepository extends MongoRepository<GenImage, String> {
    @Query(value="{'userId': ?0}")
    List<GenImage> findByUserId(Pageable pageable, String userId);
}