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
@Table(name = "boards")
public class Boards {

    @Id    // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long board_id;

    private String board_title;

    private String board_content;

    private String board_writer;

    @Column(nullable = false)
    @Builder.Default
    private Integer hitcnt = 0;

    private LocalDateTime board_created;

    private LocalDateTime board_updated;

    @Transient
    private int comment_count; // DB에 저장되지 않음, 쿼리로 값을 가져옴

}
