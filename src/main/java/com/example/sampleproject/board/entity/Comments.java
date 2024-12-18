package com.example.sampleproject.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data            // Getter Setter
@Builder        // DTO -> Entity화
@AllArgsConstructor    // 모든 컬럼 생성자 생성
@NoArgsConstructor    // 기본 생성자
@Table(name = "comments")
public class Comments {

    @Id    // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long comment_id;

    private Long board_id;

    private String comment_content;

    private String comment_writer;

    private LocalDateTime comment_created;

    private LocalDateTime comment_updated;

}
