package com.dailycodework.dreamshops.repository;

import com.dailycodework.dreamshops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Image findByProductId(Long productId);
    // No need for custom deleteByImageId as JpaRepository already provides deleteById
    // No need to declare findById as it's inherited from JpaRepository
}
