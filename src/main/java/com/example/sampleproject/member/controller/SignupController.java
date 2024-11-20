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
    public String handleStep3(SignupCommand regReq, Errors errors) {
        new RegisterRequestValidator().validate(regReq, errors);
        if (errors.hasErrors())
            return "/member/signupForm";

        try {
            memberRegisterService.regist(regReq);
            return "redirect:/";
        } catch (DuplicateMemberException ex) {
            errors.rejectValue("email", "duplicate");
            return "/member/signupForm";
        }
    }

}
