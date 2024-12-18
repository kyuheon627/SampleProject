package com.example.sampleproject.member.controller;

import com.example.sampleproject.member.spring.DuplicateMemberException;
import com.example.sampleproject.member.spring.MemberRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private MemberRegisterService memberRegisterService;

    @GetMapping("/signup")
    public String SignupForm(Model model) {
        model.addAttribute("signupCommand", new SignupCommand());
        return "/member/signupForm";
    }

    @PostMapping("/signup")
    public String SignupSuccess(SignupCommand regReq, Errors errors) {
        // 입력 값 검증
        new RegisterRequestValidator().validate(regReq, errors);

        // 에러가 존재하면 폼으로 다시 이동
        if (errors.hasErrors()) {
            return "/member/signupForm";
        }

        try {
            // 회원 등록 로직 실행
            memberRegisterService.regist(regReq);
            return "redirect:/";
        } catch (DuplicateMemberException ex) {
            if (ex.getMessage().contains("이메일")) {
                errors.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
            } else if (ex.getMessage().contains("이름")) {
                errors.rejectValue("name", "duplicate", "이미 사용 중인 이름입니다.");
            }
            return "/member/signupForm";
        }
    }


}
