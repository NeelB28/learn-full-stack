package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
    // Image findByProductId(Long productId);
    // No need for custom deleteByImageId as JpaRepository already provides deleteById
    // No need to declare findById as it's inherited from JpaRepository
}
