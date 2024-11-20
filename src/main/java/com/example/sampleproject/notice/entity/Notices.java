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

    @ColumnDefault("0") //default 0
    private Integer hitCnt;

    private LocalDateTime notice_regdate;

}
