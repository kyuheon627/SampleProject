package com.example.sampleproject;

import com.example.sampleproject.board.entity.Boards;
import com.example.sampleproject.board.spring.BoardDao;
import com.example.sampleproject.member.entity.Members;
import com.example.sampleproject.member.spring.MemberDao;
import com.example.sampleproject.notice.entity.Notices;
import com.example.sampleproject.notice.spring.NoticeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class SampleProjectApplication {

    private final MemberDao memberDao;
    private final NoticeDao noticeDao;
    private final BoardDao boardDao;

    public static void main(String[] args) {
        SpringApplication.run(SampleProjectApplication.class, args);
    }

//    @PostConstruct
//    public void init() {
//        Members members = Members.builder()
//                .email("test@email.com")
//                .password("1234")
//                .name("관리자")
//                .regdate(LocalDateTime.now())
//                .role("admin")
//                .build();
//        memberDao.insert(members);
//
//        IntStream.rangeClosed(1, 100).forEach(i->{
//            Notices notices = Notices.builder()
//                    .notice_title("공지사항"+i)
//                    .notice_content("공지내용"+i)
//                    .notice_writer("관리자")
//                    .notice_regdate(LocalDateTime.now())
//                    .build();
//            noticeDao.insert(notices);
//        });
//
//        IntStream.rangeClosed(1, 66).forEach(i->{
//            Boards boards = Boards.builder()
//                    .board_title("게시글"+i)
//                    .board_content("게시글 내용"+i)
//                    .board_writer("관리자")
//                    .board_created(LocalDateTime.now())
//                    .board_updated(LocalDateTime.now())
//                    .build();
//            boardDao.insertBoard(boards);
//        });
//    }

}
