package com.example.sampleproject.member.entity;

import com.example.sampleproject.member.spring.WrongIdPasswordException;
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
@Table(name = "members")
public class Members {

    @Id    // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private LocalDateTime regdate;

    public Members(String email, String password,
                  String name, String role, LocalDateTime regDateTime) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.regdate = regDateTime;
    }

    public Members(String email, String name, String password, LocalDateTime regdate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.regdate = regdate;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!password.equals(oldPassword))
            throw new WrongIdPasswordException();
        this.password = newPassword;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

}
