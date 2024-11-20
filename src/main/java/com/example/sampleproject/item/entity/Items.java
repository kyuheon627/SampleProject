package com.example.sampleproject.item.entity;

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
@Table(name = "items")
public class Items {

    @Id    // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long item_id;

    private String item_name;

    private String item_type;

    private String item_price;

    private String item_description;

    private String item_image;

    public Items(String item_name, String item_type, String item_price, String item_description, String item_image) {
        this.item_name = item_name;
        this.item_type = item_type;
        this.item_price = item_price;
        this.item_description = item_description;
        this.item_image = item_image;
    }
}
