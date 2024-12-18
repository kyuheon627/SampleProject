package com.example.sampleproject.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰 ID

    @Column(name = "product_id", nullable = false)
    private String productId;

    private String reviewer; // 작성자
    private String content;  // 리뷰 내용
    private LocalDateTime createdAt; // 작성 시간

}
