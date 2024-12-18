package com.example.sampleproject.board.spring;

import com.example.sampleproject.board.entity.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 댓글 추가
    public void insertComment(Comments comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO comments (board_id, comment_content, comment_writer, comment_created, comment_updated) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, comment.getBoard_id());
            pstmt.setString(2, comment.getComment_content());
            pstmt.setString(3, comment.getComment_writer());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            return pstmt;
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        comment.setComment_id(keyValue.longValue());
    }

    // 특정 게시글의 댓글 조회
    public List<Comments> findCommentsByBoardId(Long boardId) {
        String sql = "SELECT * FROM comments WHERE board_id = ? ORDER BY comment_created ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Comments.builder()
                .comment_id(rs.getLong("comment_id"))
                .board_id(rs.getLong("board_id"))
                .comment_content(rs.getString("comment_content"))
                .comment_writer(rs.getString("comment_writer"))
                .comment_created(rs.getTimestamp("comment_created").toLocalDateTime())
                .comment_updated(rs.getTimestamp("comment_updated") != null
                        ? rs.getTimestamp("comment_updated").toLocalDateTime()
                        : null)
                .build(), boardId);
    }

    public int countCommentsByBoardId(Long boardId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE board_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, boardId);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }
}
