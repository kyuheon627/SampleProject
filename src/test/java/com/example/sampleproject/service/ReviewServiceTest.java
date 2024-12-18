package com.example.sampleproject.service;

import com.example.sampleproject.product.entity.Review;
import com.example.sampleproject.product.repository.ReviewRepository;
import com.example.sampleproject.product.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void testGetReviewsByProductId() {
        // Given
        String productId = "123";
        List<Review> mockReviews = Arrays.asList(
                new Review(1L, "123", "John Doe", "Great product!", LocalDateTime.now()),
                new Review(2L, "123", "Jane Doe", "Not bad.", LocalDateTime.now())
        );

        when(reviewRepository.findByProductId(productId)).thenReturn(mockReviews);

        // When
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        // Then
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertEquals("John Doe", reviews.get(0).getReviewer());
        assertEquals("Not bad.", reviews.get(1).getContent());
        verify(reviewRepository, times(1)).findByProductId(productId);
    }

    @Test
    void testSaveReview() {
        // Given
        Review review = new Review(null, "123", "John Doe", "Great product!", LocalDateTime.now());
        Review savedReview = new Review(1L, "123", "John Doe", "Great product!", LocalDateTime.now());

        when(reviewRepository.save(review)).thenReturn(savedReview);

        // When
        Review result = reviewService.saveReview(review);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123", result.getProductId());
        assertEquals("John Doe", result.getReviewer());
        verify(reviewRepository, times(1)).save(review);
    }
}
