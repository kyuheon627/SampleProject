package com.example.sampleproject.notice.controller;

import com.example.sampleproject.member.spring.AuthInfo;
import com.example.sampleproject.notice.entity.Notices;
import com.example.sampleproject.notice.spring.NoticeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class NoticeController {

    @Autowired
    private NoticeDao noticeDao;

    @GetMapping("/notice")
    public String showNoticeList(Model model, @SessionAttribute(name = "authInfo", required = false) AuthInfo authInfo,
                                 @PageableDefault(page = 0, size = 10) Pageable pageable) throws Exception{
        // authInfo가 존재하고, role이 admin인지 확인
        if (authInfo != null) {
            model.addAttribute("role", authInfo.getRole());
        }

        // 페이지 시작 위치와 페이지 크기 설정
        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // 페이징된 공지사항 리스트와 전체 공지사항 개수 가져오기
        List<Notices> notices = noticeDao.findNoticesWithPaging(offset, pageSize);
        int totalNotices = noticeDao.countNotices();

        // Page 객체를 생성하여 모델에 추가
        Page<Notices> page = new PageImpl<>(notices, pageable, totalNotices);
        model.addAttribute("list", page);

        // 로깅
        log.info("총 페이지 수: {}", page.getTotalPages());
        log.info("전체 개수: {}", page.getTotalElements());
        log.info("현재 페이지 번호: {}", page.getNumber());
        log.info("페이지당 데이터 개수: {}", page.getSize());
        log.info("다음 페이지 존재 여부: {}", page.hasNext());
        log.info("이전 페이지 존재 여부: {}", page.hasPrevious());
        log.info("시작페이지(0) 입니까: {}", page.isFirst());

        model.addAttribute("list", page);
        return "notice/list";
    }

    // 공지사항 세부 정보 조회 메서드
    @GetMapping("/notice/detail")
    public String showNoticeDetail(@RequestParam("id") Long id, Model model) {
        // 요청된 id 파라미터 로깅
        log.info("요청된 공지사항 ID: {}", id);

        // 공지사항 세부 정보 조회
        Optional<Notices> notice = noticeDao.findNoticeById(id);

        // 공지사항이 존재하는지 확인하고 모델에 추가
        if (notice.isPresent()) {
            model.addAttribute("notice", notice.get());
            return "notice/detail";
        } else {
            // 공지사항이 없을 경우 에러 페이지 또는 목록 페이지로 리다이렉트
            return "redirect:/notice";
        }
    }

}
