package com.example.sampleproject.item.spring;

import com.example.sampleproject.item.entity.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class ItemDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Items items) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                // 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
                PreparedStatement pstmt = con.prepareStatement(
                        "insert into ITEMS (ITEM_NAME, ITEM_TYPE, ITEM_PRICE, ITEM_IMAGE, ITEM_DESCRIPTION) " +
                                "values (?, ?, ?, ?, ?)",
                        new String[] { "ITEM_ID" });
                // 인덱스 파라미터 값 설정
                pstmt.setString(1, items.getItem_name());
                pstmt.setString(2, items.getItem_type());
                pstmt.setString(3, items.getItem_price());
                pstmt.setString(4, items.getItem_image());
                pstmt.setString(5, items.getItem_description());
                // 생성한 PreparedStatement 객체 리턴
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        items.setItem_id(keyValue.longValue());
    }

}
