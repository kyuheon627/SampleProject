package com.example.sampleproject.main.controller;

import com.example.sampleproject.board.entity.Boards;
import com.example.sampleproject.board.spring.BoardDao;
import com.example.sampleproject.notice.entity.Notices;
import com.example.sampleproject.notice.spring.NoticeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class MainController {

    private final NoticeDao noticeDao;
    private final BoardDao boardDao;

    @Autowired
    public MainController(NoticeDao noticeDao, BoardDao boardDao) {
        this.noticeDao = noticeDao;
        this.boardDao = boardDao;
    }

    @GetMapping("/")
    public String MainPage(Model model) {
        log.info("Loading main page...");

        // 공지사항 5개 가져오기
        List<Notices> recentNotices = noticeDao.findNoticesWithPaging(0, 5);
        log.info("Fetched {} notices", recentNotices.size());
        recentNotices.forEach(notice -> log.debug("Notice: {}", notice));

        model.addAttribute("notices", recentNotices);

        // 게시판 게시글 5개 가져오기
        List<Boards> recentBoards = boardDao.findBoardsWithPaging(0, 5);
        log.info("Fetched {} boards", recentBoards.size());
        recentBoards.forEach(board -> log.debug("Board: {}", board));

        model.addAttribute("boards", recentBoards);

        return "/main/home";
    }
}
