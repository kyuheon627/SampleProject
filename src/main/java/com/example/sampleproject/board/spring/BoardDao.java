package com.example.sampleproject.board.spring;

import com.example.sampleproject.board.entity.Boards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BoardDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BoardDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 페이징 처리를 위한 게시글 조회 메서드
    public List<Boards> findBoardsWithPaging(int offset, int limit) {
        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments c WHERE c.board_id = b.board_id) AS comment_count " +
                "FROM boards b ORDER BY b.board_id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Boards.builder()
                .board_id(rs.getLong("board_id"))
                .board_title(rs.getString("board_title"))
                .board_content(rs.getString("board_content"))
                .board_writer(rs.getString("board_writer"))
                .hitcnt(rs.getInt("hitcnt"))
                .board_created(rs.getTimestamp("board_created").toLocalDateTime())
                .board_updated(rs.getTimestamp("board_updated") != null
                        ? rs.getTimestamp("board_updated").toLocalDateTime()
                        : null)
                .comment_count(rs.getInt("comment_count"))
                .build(), limit, offset);
    }

    // 게시글 전체 개수를 반환하는 메서드
    public int countBoards() {
        String sql = "SELECT COUNT(*) FROM boards";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 모든 게시글 조회
    public List<Boards> findAllBoards() {
        String sql = "SELECT * FROM boards ORDER BY board_id DESC";
        return jdbcTemplate.query(sql, boardRowMapper());
    }

    // 특정 게시글 조회
    public Optional<Boards> findBoardById(Long id) {
        String sql = "SELECT * FROM boards WHERE board_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, boardRowMapper()).stream().findFirst();
    }

    // 특정 게시글의 댓글 조회
    public List<Map<String, Object>> findAllBoardsWithCommentCount() {
        String sql = "SELECT b.board_id, b.board_title, b.board_writer, b.hitcnt, b.board_created, " +
                "(SELECT COUNT(*) FROM comments c WHERE c.board_id = b.board_id) AS comment_count " +
                "FROM boards b ORDER BY b.board_created DESC";

        return jdbcTemplate.queryForList(sql);
    }

    // 게시글 생성
    public void insertBoard(Boards board) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO boards (board_title, board_content, board_writer, hitcnt, board_created, board_updated) " +
                                "VALUES (?, ?, ?, ?, ?, ?)", new String[]{"board_id"});
                pstmt.setString(1, board.getBoard_title());
                pstmt.setString(2, board.getBoard_content());
                pstmt.setString(3, board.getBoard_writer());
                pstmt.setInt(4,  0);
                pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        board.setBoard_id(keyValue.longValue());
    }

    // 게시글 수정
    public void updateBoard(Boards board) {
        String sql = "UPDATE boards SET board_title = ?, board_content = ?, board_writer = ?, hitcnt = ?, board_updated = ? WHERE board_id = ?";
        jdbcTemplate.update(sql, board.getBoard_title(), board.getBoard_content(), board.getBoard_writer(),
                board.getHitcnt(), Timestamp.valueOf(board.getBoard_updated()), board.getBoard_id());
    }

    // 게시글 삭제
    public void deleteBoard(Long board_id) {
        String sql = "DELETE FROM boards WHERE board_id = ?";
        jdbcTemplate.update(sql, board_id);
    }

    // RowMapper 구현
    private RowMapper<Boards> boardRowMapper() {
        return (rs, rowNum) -> Boards.builder()
                .board_id(rs.getLong("board_id"))
                .board_title(rs.getString("board_title"))
                .board_content(rs.getString("board_content"))
                .board_writer(rs.getString("board_writer"))
                .hitcnt(rs.getInt("hitcnt"))
                .board_created(rs.getTimestamp("board_created").toLocalDateTime())
                .board_updated(rs.getTimestamp("board_updated") != null
                        ? rs.getTimestamp("board_updated").toLocalDateTime()
                        : null)
                .build();
    }

}
