package com.example.sampleproject;

import com.example.sampleproject.item.entity.Items;
import com.example.sampleproject.item.spring.ItemDao;
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
    private final ItemDao itemDao;
    private final NoticeDao noticeDao;

    public static void main(String[] args) {
        SpringApplication.run(SampleProjectApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Members members = Members.builder()
                .email("test@email.com")
                .password("1234")
                .name("관리자")
                .regdate(LocalDateTime.now())
                .role("admin")
                .build();
        memberDao.insert(members);

        IntStream.rangeClosed(1, 100).forEach(i->{
            Items items = Items.builder()
                    .item_name("삼다수" + i)
                    .item_type("음류")
                    .item_price("1500")
                    .item_image("images/samdasu.jpg")
                    .item_description("시원한 제주 삼다수" + i)
                    .build();
            itemDao.insert(items);
        });

        IntStream.rangeClosed(1, 100).forEach(i->{
            Notices notices = Notices.builder()
                    .notice_title("공지사항"+i)
                    .notice_content("공지내용"+i)
                    .notice_writer("관리자")
                    .notice_regdate(LocalDateTime.now())
                    .build();
            noticeDao.insert(notices);
        });
    }

}
