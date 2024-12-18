package com.example.sampleproject.member.spring;

import com.example.sampleproject.member.controller.SignupCommand;
import com.example.sampleproject.member.entity.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberRegisterService {

	@Autowired
	private MemberDao memberDao;

	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public Long regist(SignupCommand req) {
		// 이메일 중복 확인
		Members existingMemberByEmail = memberDao.selectByEmail(req.getEmail());
		if (existingMemberByEmail != null) {
			throw new DuplicateMemberException("중복된 이메일: " + req.getEmail());
		}

		// 이름 중복 확인
		Members existingMemberByName = memberDao.selectByName(req.getName());
		if (existingMemberByName != null) {
			throw new DuplicateMemberException("중복된 이름: " + req.getName());
		}

		// 새로운 사용자 등록
		Members newMember = new Members(
				req.getEmail(), req.getName(), req.getPassword(),
				LocalDateTime.now());
		memberDao.insert(newMember);
		return newMember.getId();
	}

}
