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
		Members members = memberDao.selectByEmail(req.getEmail());
		if (members != null) {
			throw new DuplicateMemberException("dup email " + req.getEmail());
		}
		Members newMember = new Members(
				req.getEmail(), req.getName(), req.getPassword(),
				LocalDateTime.now());
		memberDao.insert(newMember);
		return newMember.getId();
	}
}
