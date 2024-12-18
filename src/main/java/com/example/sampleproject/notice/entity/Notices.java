package com.example.sampleproject.notice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data            // Getter Setter
@Builder        // DTO -> Entity화
@AllArgsConstructor    // 모든 컬럼 생성자 생성
@NoArgsConstructor    // 기본 생성자
@Table(name = "notices")
public class Notices {

    @Id    // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long notice_id;

    private String notice_title;

    private String notice_content;

    private String notice_writer;

    @Column(nullable = false)
    @Builder.Default
    private Integer hitcnt = 0;

    private LocalDateTime notice_regdate;

}
