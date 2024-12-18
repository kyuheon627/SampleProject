package com.example.sampleproject.board.controller;

import com.example.sampleproject.board.entity.Boards;
import com.example.sampleproject.board.entity.Comments;
import com.example.sampleproject.board.spring.BoardDao;
import com.example.sampleproject.board.spring.CommentDao;
import com.example.sampleproject.member.spring.AuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardDao boardDao;
    @Autowired
    private CommentDao commentDao;

    @GetMapping("/board")
    public String showBoardList(Model model,  @PageableDefault(page = 0, size = 10) Pageable pageable) {


        // 페이지 시작 위치와 페이지 크기 설정
        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // 게시글 가져오기
        List<Boards> boards = boardDao.findBoardsWithPaging(offset, pageSize);

        // 게시글 전체 개수 가져오기
        int totalBoards = boardDao.countBoards();

        // boards가 null이거나 비어있을 경우 초기화
        if (boards == null) {
            boards = new ArrayList<>();
        }

        // Page 객체 생성
        Page<Boards> page = new PageImpl<>(boards, pageable, totalBoards);

        // 모델에 데이터 추가
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
        return "/board/list";
    }

    // 게시글 세부 정보 조회 메서드
    @GetMapping("/board/detail")
    public String showBoardDetail(@RequestParam("id") Long id, Model model, @SessionAttribute(name = "authInfo", required = false) AuthInfo authInfo) {
        // 요청된 id 파라미터 로깅
        log.info("요청된 공지사항 ID: {}", id);

        // authInfo가 존재하고, role이 admin인지 확인
        if (authInfo != null) {
            model.addAttribute("name", authInfo.getName());
        }

        // 게시글 세부 정보 조회
        Optional<Boards> board = boardDao.findBoardById(id);

        // 게시글이 존재하는지 확인하고 모델에 추가
        if (board.isPresent()) {

            // 조회수 증가
            Boards foundBoard = board.get();
            foundBoard.setHitcnt(foundBoard.getHitcnt() + 1);
            boardDao.updateBoard(foundBoard);

            model.addAttribute("board", board.get());

            // 댓글 데이터 추가
            List<Comments> comments = commentDao.findCommentsByBoardId(id);
            model.addAttribute("comments", comments);

            return "/board/detail";
        } else {
            // 게시글이 없을 경우 에러 페이지 또는 목록 페이지로 리다이렉트
            return "redirect:/board";
        }
    }

    // 댓글 저장
    @PostMapping("/board/{boardId}/comment")
    public String addComment(@PathVariable Long boardId, @ModelAttribute Comments comment, @SessionAttribute(name = "authInfo", required = false) AuthInfo authInfo) {
        if (authInfo == null) {
            return "redirect:/login";
        }

        comment.setBoard_id(boardId);
        comment.setComment_writer(authInfo.getName());
        commentDao.insertComment(comment);

        return "redirect:/board/detail?id=" + boardId;
    }

    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String showWriteBoardPage(Model model, @SessionAttribute(name = "authInfo", required = false) AuthInfo authInfo) {
        // authInfo을 통해 로그인 여부 확인
        if (authInfo != null) {
            model.addAttribute("board", new Boards()); // 빈 객체를 전달
            return "/board/write";
        } else {
            // 로그인이 안됐을 시 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
    }

    // 게시글 저장
    @PostMapping("/board/save")
    public String saveBoard(@ModelAttribute Boards board, @SessionAttribute(name = "authInfo", required = false) AuthInfo authInfo) {

        // 작성자 정보 추가
        board.setBoard_writer(authInfo.getName());

        // 새 게시글 저장
        boardDao.insertBoard(board);

        // 로깅
        log.info("새 게시글 저장됨: {}", board);

        return "redirect:/board";
    }

    // 수정 폼 표시
    @GetMapping("/board/edit")
    public String showEditBoard(@RequestParam("id") Long id, Model model) {
        // 요청된 id 파라미터 로깅
        log.info("요청된 게시글 ID: {}", id);

        Boards board = boardDao.findBoardById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        model.addAttribute("board", board);

        model.addAttribute("board", board);
        return "/board/edit"; // 수정 폼 템플릿 호출
    }

    // 수정 요청 처리
    @PostMapping("/board/update")
    public String updateBoard(@RequestParam("id") Long id, @ModelAttribute Boards board) {
        // 기존 게시글 데이터 조회
        Boards existingBoard = boardDao.findBoardById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));

        // 기존 데이터 수정
        existingBoard.setBoard_title(board.getBoard_title());
        existingBoard.setBoard_content(board.getBoard_content());
        existingBoard.setBoard_updated(java.time.LocalDateTime.now());

        // 업데이트 실행
        boardDao.updateBoard(existingBoard);

        return "redirect:/board"; // 수정 후 게시글 목록으로 리다이렉트
    }

    // 게시글 삭제
    @RequestMapping("board/delete")
    public String deleteBoard(@RequestParam("id") Long id) throws Exception{
        boardDao.deleteBoard(id);
        return "redirect:/board";
    }

}
