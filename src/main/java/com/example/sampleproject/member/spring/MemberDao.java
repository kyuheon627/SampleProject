package com.example.sampleproject.member.spring;

import com.example.sampleproject.member.entity.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MemberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private RowMapper<Members> memRowMapper =
			new RowMapper<Members>() {
				@Override
				public Members mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Members members = new Members(rs.getString("EMAIL"),
							rs.getString("PASSWORD"),
							rs.getString("NAME"),
							rs.getString("ROLE"),
							rs.getTimestamp("REGDATE").toLocalDateTime());
					members.setId(rs.getLong("ID"));
					return members;
				}
			};

	public Members selectByEmail(String email) {
		List<Members> results = jdbcTemplate.query(
				"select * from MEMBERS where EMAIL = ?",
				new RowMapper<Members>() {
					@Override
					public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
						Members members = new Members(
								rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								rs.getString("ROLE"),
								rs.getTimestamp("REGDATE").toLocalDateTime());
						members.setId(rs.getLong("ID"));
						return members;
					}
				}, email);

		return results.isEmpty() ? null : results.get(0);
	}

	public void insert(Members members) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				if (members.getRole() == null) {
					members.setRole("user");
				}

				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBERS (EMAIL, NAME, PASSWORD, REGDATE, ROLE) " +
						"values (?, ?, ?, ?, ?)",
						new String[] { "ID" });
				// 인덱스 파라미터 값 설정
				pstmt.setString(1, members.getEmail());
				pstmt.setString(2, members.getName());
				pstmt.setString(3, members.getPassword());
				pstmt.setTimestamp(4,
						Timestamp.valueOf(members.getRegdate()));
				pstmt.setString(5, members.getRole());
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		members.setId(keyValue.longValue());
	}

	public void update(Members members) {
		jdbcTemplate.update(
				"update MEMBERS set NAME = ?, PASSWORD = ? where EMAIL = ?",
				members.getName(), members.getPassword(), members.getEmail());
	}

	public List<Members> selectAll() {
		List<Members> results = jdbcTemplate.query("select * from MEMBERS",
				(ResultSet rs, int rowNum) -> {
					Members members = new Members(
							rs.getString("EMAIL"),
							rs.getString("PASSWORD"),
							rs.getString("NAME"),
							rs.getString("ROLE"),
							rs.getTimestamp("REGDATE").toLocalDateTime());
					members.setId(rs.getLong("ID"));
					return members;
				});
		return results;
	}

	public List<Members> selectByRegdate(LocalDateTime from, LocalDateTime to) {
		List<Members> results = jdbcTemplate.query("select * from MEMBERS where REGDATE between ? and ? " + "order by REGDATE desc",
				new RowMapper<Members>() {
					@Override
					public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
						Members members = new Members(
								rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								rs.getString("ROLE"),
								rs.getTimestamp("REGDATE").toLocalDateTime());
						members.setId(rs.getLong("ID"));
						return members;
					}
				},
				from, to);
		return results;
	}

	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBERS", Integer.class);
		return count;
	}

	public Members selectById(Long memId) {
		List<Members> results = jdbcTemplate.query(
				"select * from MEMBERS where ID = ?",
				memRowMapper, memId);

		return results.isEmpty() ? null : results.get(0);
	}

}
