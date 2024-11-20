package com.example.sampleproject.member.spring;

import com.example.sampleproject.member.entity.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private MemberDao memberDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public AuthInfo authenticate(String email, String password) {
		Members members = memberDao.selectByEmail(email);
		if(members == null) {
			throw new WrongIdPasswordException();
		}
		if(!members.matchPassword(password)) {
			throw new WrongIdPasswordException();
		}
		return new AuthInfo(members.getId(),
				members.getEmail(),
				members.getName(),
				members.getRole());
	}
	
}
