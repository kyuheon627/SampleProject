package com.example.sampleproject.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data            // Getter Setter
@Builder        // DTO -> Entity화
@AllArgsConstructor    // 모든 컬럼 생성자 생성
@NoArgsConstructor    // 기본 생성자
@Table(name = "products")
public class Product {

    @Id    // PK
    private String id;         // 상품 ID

    private String name;       // 상품명

    private String category;   // 카테고리

    private String price;         // 가격

    private String event; // 행사 종류

    private int stockAmount;   // 재고량 추가

    private String imageUrl;   // 이미지 URL



}
