package com.example.sampleproject.member.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LoginCommandValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "이메일을 입력해 주세요.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "비밀번호를 입력해 주세요.");
	}

}
