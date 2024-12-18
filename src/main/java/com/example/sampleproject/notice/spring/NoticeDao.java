package com.example.sampleproject.notice.spring;

import com.example.sampleproject.notice.entity.Notices;
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
import java.util.Optional;

@Repository
public class NoticeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public NoticeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 페이징 처리를 위한 공지사항 조회 메서드
    public List<Notices> findNoticesWithPaging(int offset, int pageSize) {
        String sql = "SELECT * FROM notices ORDER BY notice_id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, noticeRowMapper());
    }

    // 공지사항 전체 개수를 반환하는 메서드
    public int countNotices() {
        String sql = "SELECT COUNT(*) FROM notices";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 특정 공지사항 조회 메서드
    public Optional<Notices> findNoticeById(Long id) {
        String sql = "SELECT * FROM notices WHERE notice_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, noticeRowMapper()).stream().findFirst();
    }


    public void insert(Notices notices) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                // 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
                PreparedStatement pstmt = con.prepareStatement(
                        "insert into NOTICES (notice_title, notice_content, notice_writer, hitcnt, notice_regdate) " +
                                "values (?, ?, ?, ?, ?)",
                        new String[] { "ITEM_ID" });
                // 인덱스 파라미터 값 설정
                pstmt.setString(1, notices.getNotice_title());
                pstmt.setString(2, notices.getNotice_content());
                pstmt.setString(3, notices.getNotice_writer());
                pstmt.setInt(4, 0);
                pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                // 생성한 PreparedStatement 객체 리턴
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        notices.setNotice_id(keyValue.longValue());
    }

    // 공지사항 수정
    public void updateNotice(Notices notice) {
        String sql = "UPDATE notices SET notice_title = ?, notice_content = ?, notice_writer = ?, hitcnt = ? WHERE notice_id = ?";
        jdbcTemplate.update(sql, notice.getNotice_title(), notice.getNotice_content(), notice.getNotice_writer(),
                notice.getHitcnt(), notice.getNotice_id());
    }

    // 특정 공지사항 삭제
    public void deleteNotice(Long notice_id) {
        String sql = "DELETE FROM notices WHERE notice_id = ?";
        jdbcTemplate.update(sql, notice_id);
    }

    private RowMapper<Notices> noticeRowMapper() {
        return (rs, rowNum) -> Notices.builder()
                .notice_id(rs.getLong("notice_id"))
                .notice_title(rs.getString("notice_title"))
                .notice_content(rs.getString("notice_content"))
                .notice_writer(rs.getString("notice_writer"))
                .hitcnt(rs.getInt("hitcnt"))
                .notice_regdate(rs.getTimestamp("notice_regdate").toLocalDateTime())
                .build();
    }

}
