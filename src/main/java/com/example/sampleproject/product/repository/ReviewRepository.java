package com.example.sampleproject.product.repository;

import com.example.sampleproject.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(String productId); // 상품 ID로 리뷰 조회
}
