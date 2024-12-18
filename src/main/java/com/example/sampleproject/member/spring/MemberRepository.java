package com.example.sampleproject.member.spring;

import com.example.sampleproject.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Members, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}