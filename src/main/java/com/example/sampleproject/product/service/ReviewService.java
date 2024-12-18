package com.example.sampleproject.product.service;

import com.example.sampleproject.product.entity.Review;
import com.example.sampleproject.product.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 생성자 주입
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
}